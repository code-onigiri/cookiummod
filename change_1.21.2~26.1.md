# NeoForge 1.21.1～26.1スナップショット 変更点まとめ

---
変更点
`new MyBlock(BlockBehaviour.Properties.of().xxx())`→`new MyBlock(BlockBehaviour.Properties.of().xxx().setId(ResourceKey.create(Registries.BLOCK, resourceLocation)))`
説明
BlockのPropertiesにIDを明示的に設定する必要が生じました。`DeferredRegister.Blocks.registerBlock`を使用すれば自動的に設定されます。
---

---
変更点
`new BlockEntityType.Builder.of(MyBlockEntity::new, BLOCK1.get(), BLOCK2.get()).build(null)`→`new BlockEntityType<>(MyBlockEntity::new, BLOCK1.get(), BLOCK2.get())`
説明
BlockEntityTypeの作成方法がシンプルになり、Builderパターンが削除されました。
---

---
変更点
`registryAccess.registryOrThrow(Registries.DAMAGE_TYPE)`→`registryAccess.lookupOrThrow(Registries.DAMAGE_TYPE)`
説明
Registryアクセスメソッド名が`registry`から`lookup`に変更されました。
---

---
変更点
`registry.getHolderOrThrow(resourceKey)`→`registry.getOrThrow(resourceKey)`
説明
RegistryのgetHolderメソッドがgetにリネームされ、Holderを直接返すようになりました。
---

---
変更点
`registry.get(resourceLocation)`→`registry.getValue(resourceLocation)`
説明
直接オブジェクトを取得するメソッド名がgetValueに変更されました。
---

---
変更点
`guiGraphics.blit(TEXTURE, ...);`→`guiGraphics.blit(RenderType::guiTextured, TEXTURE, ...);`
説明
GuiGraphics.blitの第一引数にRenderType関数を明示的に指定する必要ができました。
---

---
変更点
`{ "item": "minecraft:diamond" }`→`"minecraft:diamond"`
説明
IngredientのJSON形式が簡略化され、単純な文字列でアイテムを指定できるようになりました。
---

---
変更点
`{ "tag": "c:ingots/copper" }`→`"#c:ingots/copper"`
説明
タグ指定も#プレフィックスを使った簡略形式に変更されました。
---

---
変更点
`event.addProvider(event.includeClient(), new Provider(...));`→`event.addProvider(new Provider(...));`
説明
GatherDataEvent.Client/Serverに分割され、includeClient/includeServerメソッドが削除されました。
---

---
変更点
`runs { data { ... } }`→`runs { clientData { ... } }`
説明
GradleのdataランタイムがclientDataにリネームされ、クライアントアセットとサーバーデータ生成が分離されました。
---

---
変更点
`new SwordItem(...)`→`new Item(...).component(DataComponents.WEAPON, ...)`
説明
武器、ツール、防具がデータコンポーネントベースに移行し、専用クラスが削除されました。
---

---
変更点
`new ArmorItem(...)`→`new Item(...).component(DataComponents.ARMOR, ...)`
説明
防具もデータコンポーネント化され、ArmorItemクラスが削除されました。
---

---
変更点
`BlockEntityRenderer`の`render`メソッド→`createRenderState`/`extractRenderState`/`submit`メソッド群
説明
ブロックエンティティレンダラーがレンダーステートシステムに移行し、単一のrenderメソッドが3つのメソッドに分割されました。
---

---
変更点
`EntityRenderer`の`MultiBufferSource`→`SubmitNodeCollector`
説明
エンティティレンダリングがステートベースのSubmitNodeCollectorシステムに移行しました。
---

---
変更点
`protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries)`→`protected void saveAdditional(ValueOutput output)`
説明
シリアライゼーションがCompoundTagからValueInput/ValueOutput抽象化層に移行しました。
---

---
変更点
`protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries)`→`protected void loadAdditional(ValueInput input)`
説明
デシリアライゼーションもValueInputを使用するようになりました。
---

---
変更点
`graphics.drawCenteredString(font, ..., 0xffffff);`→`graphics.drawCenteredString(font, ..., 0xffffffff);`
説明
GUI文字列描画にアルファ値の指定が必須になり、色コードにアルファ値を含める必要があります。
---

---
変更点
`guiGraphics.blitSprite(RenderType::guiTextured, ...);`→`guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURE, ...);`
説明
GUIスプライト描画がRenderPipelinesを使用するようになりました。
---

---
変更点
`FMLEnvironment.dist`→`FMLEnvironment.getDist()`
説明
FMLの状態アクセスが静的フィールドからメソッド呼び出しに変更されました。
---

---
変更点
`FMLLoader.getGamePath()`→`FMLLoader.getCurrent().getGameDir()`
説明
FMLのゲームディレクトリアクセス方法が変更され、現在のコンテキストを経由するようになりました。
---

---
変更点
`dependencies { jarJar(implementation(...)); additionalRuntimeClasspath ... }`→`dependencies { jarJar(implementation(...)) }`
説明
FMLがクラスパスからすべてをロードするようになり、additionalRuntimeClasspathの指定が不要になりました。
---

---
変更点
`IItemHandler`→`ResourceHandler<ItemResource>`
説明
アイテム転送APIがリソースベースの新システムに完全リワークされました。
---

---
変更点
`IFluidHandler`→`ResourceHandler<FluidResource>`
説明
液体転送APIもResourceHandlerベースに移行しました。
---

---
変更点
`IEnergyStorage`→`EnergyHandler`
説明
エネルギーストレージAPIが独自のEnergyHandlerインターフェースに移行しました。
---

---
変更点
`handler.insertItem(...)`→`handler.insert(index, resource, amount, transaction)`
説明
転送操作がトランザクションコンテキストを必要とするようになり、原子性が保証されるようになりました。
---

---
変更点
`Transaction`なし→`try (Transaction tx = Transaction.openRoot()) { ... tx.commit(); }`
説明
すべての転送操作がトランザクションシステムを使用するようになり、try-with-resourcesで管理します。
---

---
変更点
`Capabilities.ItemHandler.BLOCK`→`Capabilities.Item.BLOCK`
説明
ケーパビリティの登録場所が整理され、ネームスペースが簡略化されました。
---

---
変更点
`stack.getCapability(Capabilities.EnergyStorage.ITEM)`→`ItemAccess.forStack(stack).getCapability(Capabilities.Energy.ITEM)`
説明
アイテムケーパビリティがItemAccessコンテキストを必要とするようになり、より柔軟なアクセスが可能になりました。
---

---
変更点
`ResourceLocation.fromNamespaceAndPath("mymod", "main")`→`Identifier.fromNamespaceAndPath("mymod", "main")`
説明
ResourceLocationがIdentifierにリネームされました（Minecraft 1.21.11以降）。
---

---
変更点
`@javax.annotation.Nullable`→`@org.jspecify.annotations.Nullable`
説明
NullabilityアノテーションがJSpecify標準に移行しました。
---

---
変更点
`RenderType.guiTextured()`→`RenderTypes.guiTextured()`
説明
RenderTypeの静的メソッドがRenderTypesクラスに移動しました。
---

---
変更点
`new BakedQuad(int[] vertexData, ...)`→`new BakedQuad(Vector3fc[] positions, BakedColors colors, ...)`
説明
BakedQuadの内部データ構造がint[]から明示的なフィールドに変更され、より直感的になりました。
---

---
変更点
`{ "neoforge_data": { "block_light": 15, "sky_light": 0 } }`→`{ "light_emission": 15 }`
説明
モデルJSONのライト指定がlight_emissionに統合され、シンプルになりました。
---

---
変更点
`java.toolchain.languageVersion = JavaLanguageVersion.of(21)`→`java.toolchain.languageVersion = JavaLanguageVersion.of(25)`
説明
26.1スナップショットでJava 25が必須になりました。
---

---
変更点
`neoforge_version=21.1.113`→`neoforge_version=26.1.0.0-alpha.1+snapshot-1`
説明
バージョン表記がMinecraft 26.1に対応する新形式に変更されました。
---

---
変更点
`neoForge.parchment.minecraftVersion=1.21`→削除可能
説明
Mojangの難読化解除に伴い、Parchmentマッピングが不要になりました。
---

---
変更点
`@Mod`コンストラクタで`Minecraft.getInstance()`→`FMLClientSetupEvent`でのアクセス
説明
MOD初期化のタイミングが変更され、@ModコンストラクタではMinecraftインスタンスにアクセスできなくなりました。
---

---
変更点
`KeyMapping.Category`の文字列定義→`new KeyMapping.Category(ResourceLocation)`
説明
キーマッピングカテゴリが専用のレコード型になり、明示的な登録が必要になりました。
---

---
変更点
`RenderHighlightEvent`→`ExtractBlockOutlineRenderStateEvent` + `CustomBlockOutlineRenderer`
説明
ブロックアウトラインレンダリングイベントがステート抽出ベースの新システムに移行しました。
---

---
変更点
`ILaunchPluginService`→削除予定
説明
コアMOD変換システムが今後削除される予定で、Mixinが推奨されます。
---

---
変更点
`gradlew runData`→`gradlew runClientData`
説明
データ生成実行タスク名が明確化され、clientDataを推奨するようになりました。
---

---
変更点
`additionalRuntimeClasspath`依存性→標準の`implementation`のみ
説明
ランタイムクラスパスの指定が不要になり、標準的なGradle依存性管理が使用可能になりました。
---

---
変更点
`IItemHandler.of(newHandler)`ラッパー→新API完全移行
説明
古いAPIから新APIへのラッパーが提供されていますが、将来的には完全移行が推奨されます。
---

---
変更点
`Split SourceSets`なし→`common`と`client`に分離
説明
NeoForge本体のコードベースが共通コードとクライアントコードに分割され、MODも同様の構造に適応する必要があります。
---

---
変更点
`core-shader JSON`→`RenderPipeline`コード定義
説明
シェーダ定義がJSONからコードベースのRenderPipelineに完全に移行しました。
---

---
変更点
`GuiGraphics`直接描画→`RenderPipelines.GUI_TEXTURE`使用
説明
GUIテクスチャ描画が専用のRenderPipelineを使用するようになりました。
---

---
変更点
`ValueIOSerializable`インターフェース→`INBTSerializable`からの移行推奨
説明
新しいシリアライズインターフェースが導入され、NBT操作がより柔軟になりました。
---

---
変更点
`ItemStackHandler`→`ItemStacksResourceHandler`または`ItemAccessItemHandler`
説明
アイテムハンドラの実装クラスが新APIに対応する新クラスに置き換わりました。
---

---
変更点
`ModDevGradle 1.x`→`ModDevGradle 2.x`
説明
MOD開発用Gradleプラグインが全面的に刷新され、パフォーマンスと機能が向上しました。
---

---
変更点
`mods.toml`のmodLoaderバージョン→`2`に更新
説明
ModDevGradle 2に伴い、mods.tomlのmodLoaderバージョンが2になりました。
---

---
変更点
`RenderLevelStageEvent`→ステート抽出ベートに移行中
説明
レベルレンダリングステージイベントがレンダーステートシステムへの移行が進行中です。
---

---
変更点
`EntityEquipment`の個別スロットリスト→`EntityEquipment`統合オブジェクト
説明
エンティティ装備品のストレージが統合され、より一貫したアクセスが可能になりました。
---

---
変更点
`SimpleEquipmentLayer`なし→新規追加
説明
エンティティ装備品のレンダリングが専用のRenderLayerとして追加され、より柔軟なカスタマイズが可能になりました。
---