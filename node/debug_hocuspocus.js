import * as HocuspocusServer from '@hocuspocus/server';
console.log('Exports:', Object.keys(HocuspocusServer));
console.log('Server:', HocuspocusServer.Server);
console.log('Hocuspocus:', HocuspocusServer.Hocuspocus);
if (HocuspocusServer.Server) {
    console.log('Server.configure:', HocuspocusServer.Server.configure);
    console.log('Server prototype:', HocuspocusServer.Server.prototype);
}
