
// https://developers.google.com/closure/library/docs/depswriter - сделать экспорт-импорт
goog.provide('voc_app.fakes');  // Export?

voc_app.fakes.getFakeResponse = function () {
  // Данные которые будут приходить с веб-сервера
  var response = []
  var CONTENT = CONSTANTS.CONTENT;
  var TRANSLATE = CONSTANTS.TRANSLATE;
  response.push({
     "content" : ["Hello Display the matched. Display the matched elements with a sliding motion.", "Display the matched elements with a sliding motion."],
     "translate" : ["Перевод1", "Перевод2"],
     'word': ['matched']
  });

  response.push({
     'content' : ["Hello"],
     'translate' : ["Перевод", "Переводasdf"],
     'word': ['hello']
  });

  response.push({
         'content' : ["Hello", "Hello"],
         'translate' : ["Перевод", "Переводasdf"],
         'word': ['hello']
      });

  response.push({
             'content' : ["Hello", "Hello"],
             'translate' : ["Перевод", "Переводasdf"],
             'word': ['hello']
          });

  return response;
};

// Ensures the symbol will be visible after compiler renaming.
goog.exportSymbol('voc_app.fakes.getFakeResponse', voc_app.fakes.getFakeResponse);