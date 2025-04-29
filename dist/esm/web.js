import { WebPlugin } from '@capacitor/core';
export class FilePickerWeb extends WebPlugin {
    async pick(options) {
        console.log('pick', options);
        throw this.unimplemented('Not implemented on web.');
    }
    async pickImages(options) {
        console.log('pickImages', options);
        throw this.unimplemented('Not implemented on web.');
    }
    async pickVideos(options) {
        console.log('pickVideos', options);
        throw this.unimplemented('Not implemented on web.');
    }
    async pickFiles(options) {
        console.log('pickFiles', options);
        throw this.unimplemented('Not implemented on web.');
    }
}
//# sourceMappingURL=web.js.map