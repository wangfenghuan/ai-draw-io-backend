import { Server } from '@hocuspocus/server';

try {
    const s = new Server({ port: 1234 });
    console.log('Server instance keys:', Object.keys(s));
    console.log('Has listen?', typeof s.listen);
    console.log('Has configure?', typeof s.configure);
} catch (e) {
    console.log('Error instantiating Server:', e);
}
