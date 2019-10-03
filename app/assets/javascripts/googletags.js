//push any data-gtag objects in the format "key:value, key:value" into global dataLayer
(function (d, w) {

  function ready(fn) {
    if (d.readyState != 'loading') {
      fn();
    } else {
      d.addEventListener('DOMContentLoaded', fn);
    }
  }

  function parseData(string) {
    var properties = string.split(', ')
    var obj = {}
    properties.forEach(function(property) {
        var tup = property.split(':')
        obj[tup[0]] = tup[1]
    })
    return obj
  }

  ready(function() {
    w.dataLayer = w.dataLayer || [];
    var localData = d.querySelectorAll('[data-gtag]')
    var localObj = {
      'event': 'DOMContentLoaded',
      'Session ID': new Date().getTime() + '.' + Math.random().toString(36).substring(5),
      'Hit TimeStamp': new Date().toUTCString()
    }
    Array.prototype.forEach.call(localData, function (el, i) {
       localObj = Object.assign( localObj, parseData(el.getAttribute('data-gtag')) )
    })

    w.dataLayer.push(localObj)
  })

})(document,window);

//Google Tag Manager

(function(w,d,s,l,i){w[l]=w[l]||[];w[l].push({'gtm.start': new Date().getTime(),event:'gtm.js'});var f=d.getElementsByTagName(s)[0], j=d.createElement(s),dl=l!='dataLayer'?'&l='+l:'';j.async=true;j.src= 'https://www.googletagmanager.com/gtm.js?id='+i+dl;f.parentNode.insertBefore(j,f); })(window,document,'script','dataLayer','GTM-NDJKHWK');

// End Google Tag Manager
