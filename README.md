# capacitor-plugin-file-picker

Capacitor plugin to pick files

## Install (Capacitor 6.x)

```bash
npm install @whiteguru/capacitor-plugin-file-picker
npx cap sync
```

### Capacitor 5.x

```bash
npm install @whiteguru/capacitor-plugin-file-picker@^5.0.1
npx cap sync
```

### Capacitor 4.x

```bash
npm install @whiteguru/capacitor-plugin-file-picker@^4.0.1
npx cap sync
```

### Capacitor 3.x

```bash
npm install @whiteguru/capacitor-plugin-file-picker@3.0.1
npx cap sync
```

## API

<docgen-index>

* [`pick(...)`](#pick)
* [`pickImages(...)`](#pickimages)
* [`pickVideos(...)`](#pickvideos)
* [`pickFiles(...)`](#pickfiles)
* [Interfaces](#interfaces)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### pick(...)

```typescript
pick(options: FilePickerOptions) => Promise<FilePickerResults>
```

| Param         | Type                                                            |
| ------------- | --------------------------------------------------------------- |
| **`options`** | <code><a href="#filepickeroptions">FilePickerOptions</a></code> |

**Returns:** <code>Promise&lt;<a href="#filepickerresults">FilePickerResults</a>&gt;</code>

--------------------


### pickImages(...)

```typescript
pickImages(options?: FilePickerCommonOptions | undefined) => Promise<FilePickerResults>
```

| Param         | Type                                                                        |
| ------------- | --------------------------------------------------------------------------- |
| **`options`** | <code><a href="#filepickercommonoptions">FilePickerCommonOptions</a></code> |

**Returns:** <code>Promise&lt;<a href="#filepickerresults">FilePickerResults</a>&gt;</code>

--------------------


### pickVideos(...)

```typescript
pickVideos(options?: FilePickerCommonOptions | undefined) => Promise<FilePickerResults>
```

| Param         | Type                                                                        |
| ------------- | --------------------------------------------------------------------------- |
| **`options`** | <code><a href="#filepickercommonoptions">FilePickerCommonOptions</a></code> |

**Returns:** <code>Promise&lt;<a href="#filepickerresults">FilePickerResults</a>&gt;</code>

--------------------


### pickFiles(...)

```typescript
pickFiles(options?: FilePickerCommonOptions | undefined) => Promise<FilePickerResults>
```

| Param         | Type                                                                        |
| ------------- | --------------------------------------------------------------------------- |
| **`options`** | <code><a href="#filepickercommonoptions">FilePickerCommonOptions</a></code> |

**Returns:** <code>Promise&lt;<a href="#filepickerresults">FilePickerResults</a>&gt;</code>

--------------------


### Interfaces


#### FilePickerResults

| Prop        | Type                            |
| ----------- | ------------------------------- |
| **`files`** | <code>FilePickerResult[]</code> |


#### FilePickerResult

| Prop            | Type                | Description                                                                                                       |
| --------------- | ------------------- | ----------------------------------------------------------------------------------------------------------------- |
| **`path`**      | <code>string</code> | File Path                                                                                                         |
| **`webPath`**   | <code>string</code> | webPath returns a path that can be used to set the src attribute of an image for efficient loading and rendering. |
| **`name`**      | <code>string</code> | File Name                                                                                                         |
| **`extension`** | <code>string</code> | File Extensions                                                                                                   |


#### FilePickerOptions

| Prop        | Type                  |
| ----------- | --------------------- |
| **`mimes`** | <code>string[]</code> |


#### FilePickerCommonOptions

| Prop           | Type                 | Description                       | Default            |
| -------------- | -------------------- | --------------------------------- | ------------------ |
| **`multiple`** | <code>boolean</code> | Select multiple Files             | <code>false</code> |
| **`limit`**    | <code>number</code>  | Maximum number of files to select |                    |

</docgen-api>
