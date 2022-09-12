self.addEventListener('install', (e) => {
    e.waitUntil(
        caches.open('sc-store').then((cache) => cache.addAll([
            '/static/sidebars.js',
            '/static/sidebars.css',
            '/static/style.css',
        ])),
    );
});

self.addEventListener('fetch', (e) => {
    console.log(e.request.url);
    e.respondWith(
        caches.match(e.request).then((response) => response || fetch(e.request)),
    );
});