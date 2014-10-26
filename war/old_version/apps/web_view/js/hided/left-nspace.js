/**
	abs. : Защищенный файл для left.htm
	
	file : LeftNamespace.js
*/
var LeftNamespace = (function () { 
/** @private */
	var _bOn;
	var _bUnLock;
	var _bFail;
	var _bFirst;	// что за флаг?
	var signLoading = '<table id="loadProgress"><tr><td><b>Подождите...<b></td></tr></table>';
		
	// если последний удаляем прогрессбар
	function _doIfLast() { if( _arr.length === 1) _removeLoadingBar(); }
	
	// вставить перед
	function _insertLoadingBar () {	$( signLoading ).insertBefore($('#leftData')); }
	function _removeLoadingBar () { 
		
	$('#loadProgress').hide(); 
	//$("#loadProgress").toggle("slow");
		//alert('block')
	//setTimeout(function() {
    //    $("#loadProgress").remove();
    //}, 3000);
	
	//setTimeout(
         //       function() {
         //           document.getElementById("loadProgress").style.display = 'none';
         //       },
         //       5000
         //   );
	
	}
	function _clear_all( ) { _bFirst=1; }
	
	// Очередь последующих запросов
	var _arr = [];

	// последний шаг обработки запросо серии - не товопого!
	function _lastStepProcessing( gettedStr ) {
		var canDoNewRequest = true;
		_doIfLast();	// до сдвига массива!
		parent.SummatorInfo.otherDone( canDoNewRequest, gettedStr );
	}
	
	//
	function _setProgressBar( msg, color ) {
		document.getElementById("loadProgress").innerHTML = msg;
		document.getElementById("loadProgress").style.backgroundColor=color;
	}
	
	// входной фильтр
	function _inputDataFilter( gettedRawStr, posConnectionState ) {
		var check = gettedRawStr.split(" ");
		var resultArray = 'None';
		if (check[0]=="The") _setProgressBar( "Модуль сбора данных и управления отключен", "#ffff00" );
		else {
			var curinput;
			var data = gettedRawStr.split("&");
			var cur = data[0].split("=");
			
			cur = data[ posConnectionState ].split("=");
			if ( parseInt(cur[1],10) == 3 ) _setProgressBar( "Нет ответа по RS", "#ff0000" ); 
			if ( parseInt(cur[1],10) == 0 )	_setProgressBar( "RS сервера остановлен", "#ffff00");
			
			// все ок! данные готовы к обработке
			if ( parseInt(cur[1],10) == 1 )	resultArray = data; 
		}
		return resultArray;
	}
	
	function _ajx_uniProcessing( number, gettedRawStr, inRmap, nameHeader, getHtmlCode ) {
		// проверяем соединение
		var data =_inputDataFilter( gettedRawStr, 1 );
		if( data !== 'None' ) {
			var rmap = inRmap;	
			var forSerial = [ 'conn' ];
				
			// добавляем панель
			_addAnyToMainPanel( nameHeader+getHtmlCode( number ) );
			
			// заполняем эту панель
			for( var key = 0; key < forSerial.length; key++ ) {
				rmap[ forSerial[ key ] ]( data, number );
			}
		}
	}
	
	// прочие функции-обработчики
	var AjaxCallbacks = {
		ajx_tmn: function ( gettedRawStr ) {
			var data =_inputDataFilter( gettedRawStr, 33 );
			if( data !== 'None' ) {
				// ! нужно фильтрацию добавить
				// общие параметры передатчика
				var rmap = map_ajx_tmn;		

				// вся информация хранится здесь!
				var data = gettedRawStr.split( "&" );
			
				// может после сделать, чтобы начало рисовать пока проходит запрос?
				// массовая обработка - выжно чтобы порядок охода был определен, поэтому
				//   сделан вспомогательный массив
				var forSerial = [ 'nonamed' ];
				for( var key = 0; key < forSerial.length; key++ ) {
					rmap[ forSerial[ key ] ]( data );
				}
				_lastStepProcessing( gettedRawStr );
			}
		}
	};
	
	var typedMap = {
		ajx_vtv: [ 
			'<br><center><h1>ВТВ ',
			'</h1></center>', 
			map_ajx_vtv,
			getVtvParHtmlCode,
			true
		],
		ajx_vtvmod : [
			'',
			'',
			map_ajx_vtvmod,
			getVtvModHtmlCode,
			false
		],
		ajx_pab : [
			'<center><h1>БУМ ',
			'</h1></center>', 
			map_ajx_pab,
			getPABHtmlCode,
			true
		],
		ajx_bcn : [
			'<br><center><h1>БКН ',
			'</h1></center>',
			map_ajx_bcn,
			getBCNHtmlCode,
			true,
		],
		ajx_bd : [
			'<br><center><h1>БД ',
			'</h1></center>',
			map_ajx_bd,
			getBDHtmlCode,
			true
		]
	};
	
	// обобщенная заполнялка
	var _uniFill = function ( numBlocks, currentAjaxName ) {
		var i = 0;
		for ( i = 1; i < numBlocks+1 ; i++ ) {
			AjaxCallbacks[ currentAjaxName+i ] = function(x) { 
				return function( gettedRawStr ) { 
					var vector = typedMap[ currentAjaxName ];
					var header = vector[ 0 ] + x + vector[ 1 ];
					if( !vector[ 4 ] ) header = '';
					_ajx_uniProcessing( x, gettedRawStr, vector[ 2 ], header, vector[ 3 ] );
					_lastStepProcessing( gettedRawStr );
				} 
			}( i );
		}
	};
	
	// основые параметры ВТВ
	var _NUM_VTV_MAIN = 2;
	_uniFill( _NUM_VTV_MAIN, 'ajx_vtv' );
	
	// параметры модулятора ВТВ
	var _NUM_VTV_MOD = 2;
	_uniFill( _NUM_VTV_MOD, 'ajx_vtvmod' );
	
	// Добавляем Усилители
	var _NUM_BUM = 9;
	_uniFill( _NUM_BUM, 'ajx_pab' );

	// нагрузки
	var _NUM_BCN = 1;
	_uniFill( _NUM_BCN, 'ajx_bcn' );

	// детектора
	var _NUM_BD = 2;
	_uniFill( _NUM_BD, 'ajx_bd' );
	
	
	// Обработка в чистом виде 
	function _leftDataPureProcessing( data ) {
		// набор ключей, полученных с сервера соответсвует набору функц обраб. эти ключи
		var rmap = map_ajx_lft;
		
		// создание ссылок на остальные блоки
		// развилка - хорошо бы в функция выделить
		if ( _bFirst == 1 ) {
			_bFirst = 0;
			_parseCfgSystem( data );
		}
		
		_appendToLeftPanel( '<center><h1>Общие параметры передатчика</h1></center>' );
		// timestamp 
		_appendToLeftPanel( '<p align="center">Отметка времени : '+getTimeStamp()+'</p>' );
		
		// массовая обработка - выжно чтобы порядок охода был определен, поэтому
		//   сделан вспомогательный массив
		var forSerial = [ 'TrNa', 'TrOO', 'TrLc', 'Stas', /*'VtTy',*/ 'Chan', '_Pow', 'VFRW', 'MaxT' ];
		/**
		// 'NPow', 'NuVt', 'NuAB', 'NuPA', 'NuBC', 'NuDB',   
		// , 'MaxS', '_FRW', 'conn', 'cn_snmp', 'iFRWLow', 'iFRWMax', 
		// 'iPowLow', 'iPowMax', 'iTemLow', 'iTemMax', 'is0', 'is1'
		*/
		for( var key = 0; key < forSerial.length; key++ ) {
			rmap[ forSerial[ key ] ]( data );
		}
	}
	
	function _appendAObj( url, containerName, cbUpdate, cbClear ) {
		var aAjaxObj = new AjaxObj();
		aAjaxObj.update = cbUpdate;
		aAjaxObj.noOkHttpCode = function ( http_code ) { 
			if( http_code === 0) alert( "Ответ сервера не получен, проверьте соединение" ); 
		};
		
		aAjaxObj.setPage( url );
			
		// тестовый вывод для GET запроса
		aAjaxObj.setMainContainerId( 'main' );
		aAjaxObj.setContainerId( containerName );
		
		_arr.push( aAjaxObj );
	}
	
	function _fillVtv( numberVtv ) {
		var pages = [ 'ajx_vtv'+numberVtv, 'ajx_vtvmod'+numberVtv ];
		for ( j = 0; j < pages.length; j++ ) {
			var url = CgiPages[ pages[ j ] ];
			
			// TEST
			var cb = AjaxCallbacks[ pages[ j ] ]; // !! временно !!
			
			var update = cb;
			var clear =  function () {};
			_appendAObj( url, pages[ j ], update, clear );
		}
	}
	
	function _parseCfgSystem( data ) {
		// VARS
		var url;
		var update;
		var clear = function () {};	// пока ничего
		var pages = [];
		var cb; 	// callback to processing request
	
		// ключ для имени пердатчика
		var trKeyName = _getIntValue( data, 6 )-1;
		// Число PAB!! важно - Power Amp. Blocks
		var numPAB = _getIntValue( data, 2 );
		
		// ВТВ_i
		var numVtv = _getIntValue( data, 1 );
		var k;	// переменная цикла по числу БУМов
		
		// основые параметры
		page = 'ajx_tmn';
		url = CgiPages[ page ];
		update = AjaxCallbacks[ page ];
		_appendAObj( url, page, update, clear );
		
		// DOING
		var firstPartStr = "<span style='cursor: pointer;' onclick='on_vtv";
		if ( ( numVtv >= 1 ) && ( trKeyName != KV2 ) ) {
			if ( trKeyName > 3 && trKeyName < 22 ) { 
				_fillVtv(1);
			}
			// аналоговый
			if ( trKeyName <= 3 || trKeyName >= 22 ) { 
				_appendToLeftPanel( firstPartStr + "1a( )'>ВТВ 1</span>" ); 
			}
		}
		if ( ( numVtv > 1 ) && ( trKeyName != KV2 ) ) { // неравенства почему-то разные
			if ( trKeyName > 3 && trKeyName < 22 ) {
				_fillVtv(2);
			}
			// аналоговый
			if ( trKeyName <= 3 || trKeyName >= 22 ) { 
				_appendToLeftPanel( firstPartStr + "2a( )'>ВТВ 2</span>" ); 
			}
		}

		//numPAB = 1;
		for ( k = 1; k < numPAB + 1; k++ ) {
			// создаем объекты запросчики
			page = 'ajx_pab'+k;
			url = CgiPages[ page ];
			update = AjaxCallbacks[ page ];
			_appendAObj( url, page, update, clear );
		}

		// блок нагрузок
		cur = data[ 4 ].split( "=" );
		var iNumBCV = parseInt( cur[ 1 ],10 );
		for ( k = 1; k < iNumBCV + 1; k++ ) {
			// создаем объекты запросчики
			page = 'ajx_bcn'+k;
			url = CgiPages[ page ];
			update = AjaxCallbacks[ page ];
			_appendAObj( url, page, update, clear );
		}

		// а это что? блок детекторов?
		cur = data[ 5 ].split( "=" );
		var iNumDB = parseInt( cur[ 1 ],10 );
		//iNumDB = 2;
		// !! счет с 2!
		for ( k = 2; k < iNumDB + 1; k++ ) {
			// создаем объекты запросчики
			page = 'ajx_bd'+k;
			url = CgiPages[ page ];
			update = AjaxCallbacks[ page ];
			_appendAObj( url, page, update, clear );
		}
	}

	function _printLeftData( gettedRawStr ) {
		var gettedDataPkg = gettedRawStr.split( "&" );
		var len = gettedDataPkg.length;
		for( var i = 0; i < len ; i++ ) {
			var oneElement = gettedDataPkg[ i ].split( "=" );
			_appendToLeftPanel( oneElement[ 0 ]+': '+i+ ', // '+oneElement[ 1 ]+' ['+i+']<br>' );
		}
	}
	
/** @public */
	return {
		// обработик события загрузки страницы
		processGettedData : function ( gettedRawStr ) {
			var gettedArray = _inputDataFilter( gettedRawStr, 17 );

			if ( gettedArray !== 'None' ) { 
				// можем делать следующий запрос
				var canDoSerialRequest = true;
				
				// панель нужно добавить
				_addLeftPanel();
				_leftDataPureProcessing( gettedArray );
				
				// связь с верхним уровнем, который управляет запросами
				parent.SummatorInfo.firstDone( canDoSerialRequest, gettedRawStr );
			}

			// выводим полученный массив
			//_printLeftData( gettedRawStr );
		},
		
		// Обработка ошибок запроса
		noConnect : function ( http_code ) {
			if( http_code === 0) alert( "Ответ сервера не получен, проверьте соединение" ); 
		},
		
		// обработик события загрузки страницы
		onloadcheck : function ( ) {
			_bFirst = 1;
			_addAnyToMainPanel( signLoading );
		},
		
		// 
		getArrNextRequests : function() {
			return _arr;
		},
		
		// операции с прогресс баром
		insertProgressBar : function () { _insertLoadingBar(); },
		removeProgressBar : function () { _removeLoadingBar(); }
	};
})();