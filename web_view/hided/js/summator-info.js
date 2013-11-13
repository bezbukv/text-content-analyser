/**
	abs. : модуль опроса для формирования сводного отчета
	
	file : SummatorInfo.js
	
	thinks : 
		похоже придется делать очередь запросов, или как ответ получн нак 
			запускать следующий запрос. Но точно известно, что сперва нужно сделать 
			один общий запрос и по его результатам уже делать последующие запросы
			
		может перенести всю логику в обрамляющую странице если это возможно. как происх. загр. зафреймленных
			окнах?
			
	важно : информация выводится на другой фрейм, поэтому js...
		порядок запросов реализуется тут, а обработчки в js скриптах не целевой странице
*/
var SummatorInfo = (function() {
/** @private */
	// обработчки ответов
	var _processingDataCallback;
	var _noOkHttpCode;
	
	// первый объект в цепочке запросов
	var _initAjaxObj = new AjaxObj();
	
	// кнопки
	var _buttonIDs = [ "printButton" ];
	var _imagesPath = "/img/";
	
	var _onFirstResponseGetted = function (){
		// только первый элемент
		var arr = summary.LeftNamespace.getArrNextRequests();
		if ( arr.length > 0 ) { 
			var first = arr[ 0 ];
			first.doGetRequest(); // элемент не удаляем
		}
	};
	

/** @public */
	return {
		// вызывается из набора фреймов
		onLoadFrameset : function () {
			_processingDataCallback = summary.LeftNamespace.processGettedData;
			_noOkHttpCode = summary.LeftNamespace.noConnect;
			
			// первый запрос из ответа не него узнаем какие еще нужно сделать запросы
			// переопределяем методы
			_initAjaxObj.update = _processingDataCallback;
			_initAjaxObj.noOkHttpCode = _noOkHttpCode;
			
			// тестовый вывод для GET запроса
			_initAjaxObj.setMainContainerId( 'main' );
			_initAjaxObj.setContainerId( 'leftPanelTest' );
			
			// первый GET запрос - к топовой странице
			_initAjaxObj.getData( CgiPages[ "ajx_lft" ] );
		},

		// Функция печати !фрейма
		callPrint : function () {
			// сделать кроссплатформенным
			parent.summary.focus();
			parent.summary.print();
		},
		
		// обработик события загрузки страницы c кнопкой печати
		onLoadBottom : function ( ) {
			// Рисуем кнопки
			tvct.buttonHovers( _buttonIDs, _imagesPath );
		},
		
		// Запрос к топовой странице выполнен
		firstDone : function( canDoSerialRequest, gettedStr ) {
			// можно ли запускать серию запросов
			if ( canDoSerialRequest ) {
				_onFirstResponseGetted();
			}
		},
		
		// прочие
		otherDone : function ( canDoNewRequest, value ) {
			// Внимание, получаем массив аякс объектов!
			var arr = summary.LeftNamespace.getArrNextRequests();
			
			//фактически удаляем запрос из очереди
			if ( canDoNewRequest ) {
				var last = arr.shift();
				
				// посмотрим что получили
				//last.preView( value );
				
				// следующий
				if ( arr.length > 0 ) {
					arr[0].doGetRequest();
				}
			}
		}
	};
})();