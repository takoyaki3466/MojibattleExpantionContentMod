package com.takoyaki3466.mojibattle.config;

import jp.takoa.mojibattle.ability.config.RecordConfigParser;
import jp.takoa.mojibattle.ability.config.annotations.ConfigView;
import jp.takoa.mojibattle.ability.runtime.PassiveRuntime;
import jp.takoa.mojibattle.ability.runtime.SkillRuntime;
import jp.takoa.mojibattle.ability.runtime.SupportAbilityRuntime;
import jp.takoa.mojibattle.ability.runtime.SupportPassiveRuntime;
import jp.takoa.mojibattle.ability.runtime.UltimateRuntime;
import jp.takoa.mojibattle.registry.AbilityPresentation;
import jp.takoa.mojibattle.registry.MojiBattleRegister;
import jp.takoa.mojibattle.registry.RegistryHandle;
import jp.takoa.mojibattle.registry.impl.PassiveImpl;
import jp.takoa.mojibattle.registry.impl.SkillImpl;
import jp.takoa.mojibattle.registry.impl.SupportAbilityImpl;
import jp.takoa.mojibattle.registry.impl.SupportPassiveImpl;
import jp.takoa.mojibattle.registry.impl.UltimateImpl;

import java.util.Objects;
import java.util.function.Function;

/**
 * record形式のAbility設定を共通パーサーで検証し、能力ID付きで登録する。
 *
 * <p>設定エラーを戦闘中まで遅延させず、JSON検証またはRuntime生成時に
 * 能力ID、設定項目名、不正値を含む例外として報告する。</p>
 */
public final class ConfigRegistration {
    private ConfigRegistration() {
    }

    /** 設定可能なスキルを登録する。 */
    public static <C, R extends SkillRuntime> RegistryHandle<SkillImpl> skill(
            MojiBattleRegister.Skill registry, String namespace, String path,
            Class<C> configType, Function<C, R> runtimeFactory) {
        ContextualParser<C> parser = parser(namespace, path, configType);
        return registry.register(path,
                source -> runtimeFactory.apply(parser.parse(source)), parser::validate,
                (source, multiplier) -> AbilityPresentation.fromDescription(
                        source, parser.displayValues(source, multiplier)));
    }

    /** 設定可能な必殺技を登録する。 */
    public static <C, R extends UltimateRuntime> RegistryHandle<UltimateImpl> ultimate(
            MojiBattleRegister.Ultimate registry, String namespace, String path,
            Class<C> configType, Function<C, R> runtimeFactory) {
        ContextualParser<C> parser = parser(namespace, path, configType);
        return registry.register(path,
                source -> runtimeFactory.apply(parser.parse(source)), parser::validate,
                source -> AbilityPresentation.fromDescription(
                        source, parser.displayValues(source, 1f)));
    }

    /** 設定可能なパッシブを登録する。 */
    public static <C, R extends PassiveRuntime> RegistryHandle<PassiveImpl> passive(
            MojiBattleRegister.Passive registry, String namespace, String path,
            Class<C> configType, Function<C, R> runtimeFactory) {
        ContextualParser<C> parser = parser(namespace, path, configType);
        return registry.register(path,
                source -> runtimeFactory.apply(parser.parse(source)), parser::validate,
                source -> AbilityPresentation.fromDescription(
                        source, parser.displayValues(source, 1f)));
    }

    /** 設定可能な補助発動能力を登録する。 */
    public static <C, R extends SupportAbilityRuntime> RegistryHandle<SupportAbilityImpl> supportAbility(
            MojiBattleRegister.SupportAbility registry, String namespace, String path,
            Class<C> configType, Function<C, R> runtimeFactory) {
        ContextualParser<C> parser = parser(namespace, path, configType);
        return registry.register(path, parser::parse, runtimeFactory);
    }

    /** 設定可能な補助パッシブを登録する。 */
    public static <C, R extends SupportPassiveRuntime> RegistryHandle<SupportPassiveImpl> supportPassive(
            MojiBattleRegister.SupportPassive registry, String namespace, String path,
            Class<C> configType, Function<C, R> runtimeFactory) {
        ContextualParser<C> parser = parser(namespace, path, configType);
        return registry.register(path, parser::parse, runtimeFactory);
    }

    private static <C> ContextualParser<C> parser(String namespace, String path, Class<C> configType) {
        String abilityId = Objects.requireNonNull(namespace, "Namespace is required") + ":"
                + Objects.requireNonNull(path, "Ability path is required");
        return new ContextualParser<>(abilityId, configType);
    }

    private static final class ContextualParser<C> {
        private final String abilityId;
        private final RecordConfigParser<C> delegate;

        private ContextualParser(String abilityId, Class<C> configType) {
            this.abilityId = abilityId;
            try {
                delegate = new RecordConfigParser<>(configType);
            } catch (IllegalArgumentException exception) {
                throw contextual("config definition", exception);
            }
        }

        private C parse(ConfigView source) {
            try {
                return delegate.parse(source);
            } catch (IllegalArgumentException exception) {
                throw contextual("config", exception);
            }
        }

        private void validate(ConfigView source) {
            parse(source);
        }

        private Object[] displayValues(ConfigView source, float multiplier) {
            try {
                return delegate.displayValues(source, multiplier);
            } catch (IllegalArgumentException exception) {
                throw contextual("config", exception);
            }
        }

        private IllegalArgumentException contextual(String subject, IllegalArgumentException cause) {
            return new IllegalArgumentException(
                    "Ability '" + abilityId + "' " + subject + " is invalid: " + cause.getMessage(), cause);
        }
    }
}
