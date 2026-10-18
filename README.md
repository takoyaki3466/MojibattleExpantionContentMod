# MojiBattleEX Expansion Content

MojiBattleEXへメインキャラクターと補助キャラクターを追加するコンテンツMODです。

## 収録キャラクター

### メインキャラクター

| キャラクター | スキル | 必殺技 | パッシブ | 特徴 |
| --- | --- | --- | --- | --- |
| 木 | **森に帰れ**：短い間隔で小ダメージとノックバックを与える | **森羅万象**：すべての敵へ木ランクに応じたダメージを与える | **広がる森の迷路**：スキルで木を獲得し、木→林→森→森羅万象と成長する | スキルを連発して必殺技を育てる晩成型 |
| 金 | **金運上昇**：金運を獲得し、空から金塊を落とす | **金策爆破**：強力な一撃。会心時は防御無視の直撃となり、金運を最大4層消費する | **時は金なり**：金運1層につき会心率+5%。スキル時と30秒ごとに獲得し、最大10層 | 金運を貯めて会心と直撃を狙う火力型 |
| 茶 | **お茶を立てる**：攻撃と移動速度低下を与え、3秒間茶流彩彩になる | **茶流彩彩**：通常攻撃を拡散化し、攻撃間隔を短縮する | **優雅で華麗なティーパーティー**：必殺技で茶流彩彩になると必殺ゲージを50%返却する | 拡散通常攻撃を高頻度で維持する制圧型 |
| Σ | **累算**：空からΣ式を落として小ダメージを与え、累算を1層獲得する | **収束する累算**：全累算を消費し、1層につき60ダメージ。3層以上で防御無視、5層以上で確定会心 | **終わらない計算**：最大HP+50%・スキルCT短縮と引き換えにスキルダメージが大幅低下する | 高耐久を活かして累算を貯め、必殺技へ収束させる蓄積型 |

### 補助キャラクター

| キャラクター | スキル | 必殺技 | パッシブ／フィールド効果 | 特徴 |
| --- | --- | --- | --- | --- |
| 長 | **Not 短**：味方の防御力を高め、最大HPの5%を回復する | **一長一短**：味方の攻撃を速め、敵の攻撃を遅くする永続・累積強化 | **Long Long AGO**：敵味方の移動速度を下げる。「短」は対象外 | 長期戦向けの回復・速度支援 |
| dx | **平面微分**：味方メインへ次の被ダメージを一度だけ50%軽減する効果を付与する | **無限微分**：自身の通常攻撃を永続的に3段化し、攻撃間隔を短縮する | **微分領域**：全体の移動速度・攻撃力・防御力を下げる。「∫」は低下を無効化して攻撃速度が上がる | 全体デバフと単発防御を扱う特殊支援 |
| 眉 | **眉上昇**：眉レベルを10獲得し、一時的に攻撃間隔を短縮して攻撃する | **🤨🤨🤨🤨🤨🤨**：眉レベルに応じて最大20発の🤨を放ち、発動後にレベルを半減する | **ﾏﾕｦｱｹﾞﾀｶｵ**：自身または敵の通常攻撃ごとに眉レベルを1獲得し、最大200層 | 戦闘の長さを多段攻撃へ変換する蓄積型支援 |
| ㌠ | **決闘**：最もHPの多い敵と決闘し、攻撃対象を互いに限定する | **一致団結**：一定時間「サ」「ン」「チ」「ー」「ム」に分裂して共同攻撃する | **文字㌠**：メイン・補助・召喚を含む戦場人数に応じて火力が上昇する | 敵を孤立させ、人数差を火力へ変える集団戦支援 |

## Σの実装

- `AccumulationSkill`が遅延攻撃APIを使い、対象の上からΣテクスチャを落とします。攻撃成功後、パッシブ通知によって累算が1層増えます。
- `EndlessCalculationPassive`の実行インスタンスが、そのキャラクターだけの累算を保持します。同じΣが敵味方に存在しても累算は共有されません。
- `ConvergingSummationUltimate`は基礎ダメージ60を発生させ、パッシブの必殺技倍率に現在の累算数を返すことで`60 × 層数`へ変換します。3層以上では必殺技のダメージ計算中だけ防御無視、5層以上では会心率+100%を有効にします。
- 必殺技が完了すると累算をすべて消費します。累算が0層、対象が存在しない、または対象が戦闘不能の場合は発動しません。
- MojiBattle API 0.8.1には最大HP・スキルクールタイム・スキルダメージ係数を戦闘中に変更する専用フックがないため、パッシブの常時効果はΣ専用データへ反映しています。最大HPは標準1000の1.5倍となる1500、スキルCTは4秒、スキルダメージは12です。

## MODの実装構成

| 場所 | 役割 |
| --- | --- |
| `src/main/java/.../ExpansionMod.java` | 能力実装・キャラクター・フィールド効果をゲームへ登録するエントリーポイント |
| `src/main/java/.../<character>/` | スキル、必殺技、パッシブのJavaランタイム実装 |
| `src/main/resources/data/expansion_mod/characters/` | メインキャラクターの基礎能力と能力ID |
| `src/main/resources/data/expansion_mod/support/` | 補助キャラクターの基礎能力、能力、フィールド効果ID |
| `src/main/resources/data/expansion_mod/abilities/` | メインキャラクター能力のクールタイム、コスト、調整値 |
| `src/main/resources/data/expansion_mod/field_effects/` | 補助キャラクターの常時フィールド効果 |
| `src/main/resources/assets/expansion_mod/i18n/ja_jp.json` | 日本語名、説明文、状態表示 |
| `src/main/resources/assets/expansion_mod/textures/` | キャラクターと攻撃に使用する透過PNG |

## 登録方法

MojiBattle API 0.8.1には、登録先によって次の2つの方法があります。

| 方法 | 使用場所 | 登録できるもの |
| --- | --- | --- |
| `context`へ直接登録 | 外部MOD（このプロジェクトはこちら） | メイン／補助キャラクターのJSONと、メイン／補助のスキル・必殺技・パッシブRuntime |
| `DeferredRegister` | ゲーム本体または、ホストが公開した`RegistryKey<T>`を使用するモジュール | 対応する型付きRegistryの要素 |

外部MODでは、公開APIだけで完結する`context.content()`と`context.abilities()`を使用してください。`DeferredRegister`は任意の登録を遅延実行する仕組みではなく、ホスト側に同じ`RegistryKey<T>`のRegistryが作成されている場合だけ解決されます。また、0.8.1にはキャラクターデータ用の公開`RegistryKey`がないため、メイン／補助キャラクター自体はどちらの構成でも`context.content()`へ登録します。

### 方法1：`context`へ直接登録する

このプロジェクトで使用している、外部MOD向けの推奨方法です。次の例には、メインキャラクター「木」と補助キャラクター「眉」、および両者のスキル・必殺技・パッシブをすべて含めています。

```java
import com.takoyaki3466.mojibattle.eyebrow.EyebrowRaiseFacePassive;
import com.takoyaki3466.mojibattle.eyebrow.EyebrowRiseSkill;
import com.takoyaki3466.mojibattle.eyebrow.SkepticalVolleyUltimate;
import com.takoyaki3466.mojibattle.tree.ExpandingForestMazePassive;
import com.takoyaki3466.mojibattle.tree.ForestUniverseUltimate;
import com.takoyaki3466.mojibattle.tree.ReturnToForestSkill;
import jp.takoa.mojibattle.content.CharacterDataRegistration;
import jp.takoa.mojibattle.content.SupportCharacterDataRegistration;
import jp.takoa.mojibattle.mod.GameMod;
import jp.takoa.mojibattle.mod.ModContext;
import jp.takoa.mojibattle.resource.ResourceId;

public final class ExampleMod implements GameMod {
    private static final String MOD_ID = "expansion_mod";

    @Override
    public String id() {
        return MOD_ID;
    }

    @Override
    public void register(ModContext context) {
        // メインキャラクターのスキル・必殺技・パッシブ
        context.abilities().registerSkillImplementation(
                MOD_ID + ":return_to_forest",
                parameters -> new ReturnToForestSkill(
                        ReturnToForestSkill.Config.from(parameters)));
        context.abilities().registerUltimateImplementation(
                MOD_ID + ":forest_universe",
                parameters -> new ForestUniverseUltimate(
                        ForestUniverseUltimate.Config.from(parameters)));
        context.abilities().registerPassiveImplementation(
                MOD_ID + ":expanding_forest_maze",
                parameters -> new ExpandingForestMazePassive(
                        ExpandingForestMazePassive.Config.from(parameters)));

        // 補助キャラクターのスキル・必殺技・パッシブ
        context.abilities().registerSupportSkillImplementation(
                MOD_ID + ":eyebrow_rise",
                parameters -> new EyebrowRiseSkill(
                        EyebrowRiseSkill.Config.from(parameters)));
        context.abilities().registerSupportUltimateImplementation(
                MOD_ID + ":skeptical_volley",
                parameters -> new SkepticalVolleyUltimate(
                        SkepticalVolleyUltimate.Config.from(parameters)));
        context.abilities().registerSupportPassiveImplementation(
                MOD_ID + ":eyebrow_raise_face",
                parameters -> new EyebrowRaiseFacePassive(
                        EyebrowRaiseFacePassive.Config.from(parameters)));

        // メインキャラクターと3つのAbility JSONを一括登録
        context.content().registerCharacter(new CharacterDataRegistration(
                resource("characters/tree.json"),
                resource("abilities/tree_skill.json"),
                resource("abilities/tree_ultimate.json"),
                resource("abilities/tree_passive.json"),
                "i18n"));

        // 補助キャラクターはスキル・必殺技・パッシブを1つのJSONに記述する
        context.content().registerSupportCharacter(
                new SupportCharacterDataRegistration(
                        resource("support/eyebrow.json"), "i18n"));
    }

    private static ResourceId resource(String path) {
        return new ResourceId(MOD_ID, path);
    }
}
```

登録IDはJSON側の参照と一致させます。

- メインのスキル・必殺技・パッシブ：各Ability JSONの`implementationId`
- 補助のスキル・必殺技：補助キャラクターJSON内の`skill.type`と`ultimate.type`
- 補助のパッシブ：補助キャラクターJSON内の`supportPassiveImplementationId`

Factoryへ渡される`parameters`は、対応するJSONの`parameters`です。Runtimeはキャラクター個体ごとにFactoryから生成されるため、戦闘中に変化する値はstaticフィールドではなくRuntimeインスタンスへ保持します。

### 方法2：`DeferredRegister`を使用する

`DeferredRegister<T>`は、登録内容の宣言とRegistryへの確定を分離します。`register`でFactoryを宣言し、`GameMod#register`で`context.registrationBus()`へ一度だけ`attach`すると、対応する`RegisterEvent<T>`でまとめて登録されます。

次はMojiBattleEX本体の組み込みコンテンツ側で使う構成例です。メインのスキル・必殺技・パッシブ、補助のスキル・必殺技・パッシブを、それぞれ対応するRegistryへ宣言しています。

```java
// ゲーム本体側の型を使用する例。これらの型はmojibattle-api単体には含まれない。
import jp.takoa.mojibattle.registry.BuiltInRegistries;
import jp.takoa.mojibattle.registry.DeferredRegister;
import jp.takoa.mojibattle.registry.PassiveImplementation;
import jp.takoa.mojibattle.registry.RegistryHandle;
import jp.takoa.mojibattle.registry.SkillImplementation;
import jp.takoa.mojibattle.registry.SupportAbilityImplementation;
import jp.takoa.mojibattle.registry.SupportPassiveImplementation;
import jp.takoa.mojibattle.registry.UltimateImplementation;

public final class AbilityRegistryInit {
    public static final DeferredRegister<SkillImplementation> MAIN_SKILLS =
            DeferredRegister.create(
                    BuiltInRegistries.SKILL_IMPLEMENTATIONS, "examplemod");
    public static final DeferredRegister<UltimateImplementation> MAIN_ULTIMATES =
            DeferredRegister.create(
                    BuiltInRegistries.ULTIMATE_IMPLEMENTATIONS, "examplemod");
    public static final DeferredRegister<PassiveImplementation> MAIN_PASSIVES =
            DeferredRegister.create(
                    BuiltInRegistries.PASSIVE_IMPLEMENTATIONS, "examplemod");
    public static final DeferredRegister<SupportAbilityImplementation> SUPPORT_ABILITIES =
            DeferredRegister.create(
                    BuiltInRegistries.SUPPORT_ABILITY_IMPLEMENTATIONS, "examplemod");
    public static final DeferredRegister<SupportPassiveImplementation> SUPPORT_PASSIVES =
            DeferredRegister.create(
                    BuiltInRegistries.SUPPORT_PASSIVE_IMPLEMENTATIONS, "examplemod");

    // 各Supplierは、ID、Factory、validatorなどを持つ対応Implementationを返す。
    public static final RegistryHandle<SkillImplementation> MAIN_SKILL =
            MAIN_SKILLS.register("main_skill", MainImplementations::skill);
    public static final RegistryHandle<UltimateImplementation> MAIN_ULTIMATE =
            MAIN_ULTIMATES.register("main_ultimate", MainImplementations::ultimate);
    public static final RegistryHandle<PassiveImplementation> MAIN_PASSIVE =
            MAIN_PASSIVES.register("main_passive", MainImplementations::passive);

    // 補助スキルと補助必殺技は同じSupportAbilityImplementation Registryを使う。
    public static final RegistryHandle<SupportAbilityImplementation> SUPPORT_SKILL =
            SUPPORT_ABILITIES.register("support_skill", SupportImplementations::skill);
    public static final RegistryHandle<SupportAbilityImplementation> SUPPORT_ULTIMATE =
            SUPPORT_ABILITIES.register("support_ultimate", SupportImplementations::ultimate);
    public static final RegistryHandle<SupportPassiveImplementation> SUPPORT_PASSIVE =
            SUPPORT_PASSIVES.register("support_passive", SupportImplementations::passive);

    private AbilityRegistryInit() {
    }
}
```

`MainImplementations`と`SupportImplementations`は説明用のFactoryクラス名です。実際には各Supplierが、そのRegistryの要素型と一致する実装オブジェクトを返すようにします。宣言した5個の`DeferredRegister`はエントリーポイントで接続します。キャラクターデータは公開Registryの対象ではないため、同じ`register`内で`context.content()`へ直接登録します。

```java
@Override
public void register(ModContext context) {
    AbilityRegistryInit.MAIN_SKILLS.attach(context.registrationBus());
    AbilityRegistryInit.MAIN_ULTIMATES.attach(context.registrationBus());
    AbilityRegistryInit.MAIN_PASSIVES.attach(context.registrationBus());
    AbilityRegistryInit.SUPPORT_ABILITIES.attach(context.registrationBus());
    AbilityRegistryInit.SUPPORT_PASSIVES.attach(context.registrationBus());

    // メインキャラクター
    context.content().registerCharacter(new CharacterDataRegistration(
            resource("characters/example.json"),
            resource("abilities/example_skill.json"),
            resource("abilities/example_ultimate.json"),
            resource("abilities/example_passive.json"),
            "i18n"));

    // 補助キャラクター（スキル・必殺技・パッシブの設定もこのJSONに含む）
    context.content().registerSupportCharacter(
            new SupportCharacterDataRegistration(
                    resource("support/example_support.json"), "i18n"));
}
```

`RegistryHandle#get()`は登録イベントが完了するまで呼び出せません。また、`attach`後に同じ`DeferredRegister`へ項目を追加したり、同じインスタンスを2回`attach`したりすると例外になります。

新しいメインキャラクターを追加する場合は、次の順序で実装します。

1. `SkillRuntime`、`UltimateRuntime`、必要に応じて`PassiveRuntime`を実装する。
2. `data/expansion_mod/abilities/`へ3種類の能力JSONを追加する。
3. `data/expansion_mod/characters/`へキャラクターJSONを追加する。
4. テクスチャと`ja_jp.json`の翻訳キーを追加する。
5. `ExpansionMod.register`で能力実装とキャラクターデータを登録する。
6. Gradleビルドを実行し、生成JAR内にJavaクラス・JSON・テクスチャが収録されていることを確認する。

## ビルドと導入

### 必要環境

- Java Development Kit 21
- MojiBattle API 0.8.1（ローカルMavenリポジトリへ公開済みであること）

PowerShellでは次のコマンドでビルドできます。

```powershell
$env:JAVA_HOME = 'C:\Program Files\Java\jdk-21.0.12'
.\gradlew.bat clean build --no-daemon
```

生成された`build/libs/mojibattle-expantion-content-1.3.0.jar`をMojiBattleEXのMODディレクトリへ配置し、ゲームを起動してください。旧バージョンの同MODがある場合は、重複ロードを避けるため新しいJARへ置き換えてください。
