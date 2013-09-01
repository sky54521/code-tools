
var print=function(msg){
	if($('#msgId').length==0){
		$('body').append($('<div id="msgId" style="color:#f00;position:absolute;float:left;margin-top:-150px; ">'+msg+'</div>'));
	}else{
		var obj=$('#msgId');
		obj.html($('#msgId').html()+msg);
	}
}

var println=function(msg){
	var currentDate = new Date(); 
	msg='['+currentDate.format('yyyy-MM-dd hh:mm:ss,S')+'] '+msg
	if($('#msgId').length==0){
		$('body').append($('<div id="msgId" style="color:#f00;position:absolute;float:left;margin-top:-150px; ">'+msg+'<br>'+'</div>'));
	}else{
		var obj=$('#msgId');
		obj.html($('#msgId').html()+msg+'<br>');
	}
}

	/**
	 * 模拟sleep
	 * @param {}
	 *            numberMillis
	 */
var sleep=function(numberMillis) {
	var now = new Date();
	var exitTime = now.getTime() + (numberMillis);
	while (true) {
		now = new Date();
		if (now.getTime() > exitTime)
			return;
	}
}

/**
 * 格式化日期
 * @param {} fmt yyyy-MM-dd hh:mm:ss,S
 * @return {}
 */
Date.prototype.format=function(fmt) {         
    var o = {         
    "M+" : this.getMonth()+1, //月份         
    "d+" : this.getDate(), //日         
    "h+" : this.getHours()%12 == 0 ? 12 : this.getHours()%12, //小时         
    "H+" : this.getHours(), //小时         
    "m+" : this.getMinutes(), //分         
    "s+" : this.getSeconds(), //秒         
    "q+" : Math.floor((this.getMonth()+3)/3), //季度         
    "S" : this.getMilliseconds() //毫秒         
    };         
    var week = {         
    "0" : "\u65e5",         
    "1" : "\u4e00",         
    "2" : "\u4e8c",         
    "3" : "\u4e09",         
    "4" : "\u56db",         
    "5" : "\u4e94",         
    "6" : "\u516d"        
    };         
    if(/(y+)/.test(fmt)){         
        fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));         
    }         
    if(/(E+)/.test(fmt)){         
        fmt=fmt.replace(RegExp.$1, ((RegExp.$1.length>1) ? (RegExp.$1.length>2 ? "\u661f\u671f" : "\u5468") : "")+week[this.getDay()+""]);         
    }         
    for(var k in o){         
        if(new RegExp("("+ k +")").test(fmt)){         
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));         
        }         
    }         
    return fmt;         
}



