//m引入多行模式,需要在正则表达式后面添加m 选项, 这会让$边界匹配换行符(\n) 和字符串真正的结尾
//i执行大小写不敏感
//g执行全局匹配，找到所有匹配而不是找到第一个匹配就停止


//分组、非捕获性分组   (xx) (?:xx)  \1  \2 ...
//贪婪              惰性                  支配                 描述 
//?         ??        ?+       零次或一次出现
//*         *?        *+       零次或多次出现
//+         +?        ++       一次或多次出现
//{n}       {n}?      {n}+     恰好n次出现
//{n,m}     {n,m}?    {n,m}+   至少n次之多m次出现
//{n,}      {n,}?     {n,}+    至少n次出现
var toMarch6 = "1 21 2 5"; 
var regExp6 = /((?:\d+?).*?(\d+?))\1/g; //用括号括起来的部分被放到RegExp.$x中,(.*?)其中加"?"代表惰性匹配：匹配最少个字符
var result = regExp6.exec(toMarch6); //执行一次匹配一次
//var result = regExp6.exec(toMarch6); 

//var r=RegExp.$1 +"█☻♣♦♥♠★ஐ✿❀❃❁ღ❦❧☂☃➹☪❂☭☣☢☄*☠♨✄✎✟۩Ю۞"+RegExp.$1 +" "+RegExp.$2+" "+RegExp.$2
//var r=RegExp.$1+"  ۞ "+RegExp.$1 +"  ۞ "+RegExp.$2+"  ۞ "+RegExp.$2

var r=RegExp.$1+"-"+RegExp.$2 +"-"+RegExp.$3+"-"+RegExp.$4
result.toString();

//ip
var regExpIp=/^((\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.){3}(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/gim
//var regExpIp=/[^1].*(?=\.1\.1\n\)\1)/gim   //报错，按F11自动进入调试
var regExpIp=/[^1].*(?=\.1\.1\n\))/gim   //)需要转义,=不需要转义
"114.0.1.1\n)=192.168.1.2".match(regExpIp).toString();





//var toMatch = "a bat, a cat, a fAt baT, a faT cat"; 
//var reAt = /at/gi; //如果不加参数g,返回的数组只会包含一个匹配元素 
//var arrMatchs = toMatch.match(reAt); 
//arrMatchs.toString();


//反向引用    $1 —— $9
//var toMarch3 = "123123123 3211231231"; 
//var regExp3 = /(\d+)\s*(\d+)/; 
//var sNew = toMarch3.replace(regExp3,"$2 $1 "); //注意replace不改变原来的字符串,而是返回一个替换后的新字符串 
//sNew


//候选   |
//var userInput = "badWord1badWord1asdasdandBadWord2"; 
//var toMarch6 = /badword1|badword2/gi; 
//var i=0;
//var newStr1,newStr2,newStr3,newStr4;
//var newStr = userInput.replace(toMarch6, function(match){ 
//	i++;
//	if(i==1){
//		newStr1=match;
//	}
//	if(i==2){
//		newStr2=match;
//	}
//	if(i==3){
//		newStr3=match;
//	}
//    if(i==4){
//        newStr4=match;
//    }
//    return match.replace(/./g,"*"); 
//}); 
//newStr1+" "+newStr2+" "+newStr3+" "+newStr4


//前瞻  (?= xx ) (!= xx )
//var toMarch1 = "bedroom"; 
//var toMarch2 = "bedding"; 
//var bedReg = /(bed(!=m))/g; 
////bedReg.test(toMarch1); //true 
////bedReg.exec(toMarch1).toString(); //bed,bed 因此这个正则表达式返回的第一个匹配是bed,而不是bedroom,但是它只会去匹配后面跟着 room的bed,有点搞 
////RegExp.$1; //bed 
//bedReg.test(toMarch2); //false 


//边界     ^行开头    $行结尾   \b单词的边界    \B非单词的边界
//var toMarch3 = "Important word is the last one."; 
//var regExp3 = /(\w+)\.$/ ; //这边结合上面对贪婪量词的解释,有点不明白为何能匹配one?. 
//regExp3.test(toMarch3); 
//dwr(RegExp.$1);//one 

function doSwing(t){var f=new Packages.javax.swing.JFrame(t);f.setSize(400,300);f.setVisible(true);}
doSwing("frame");
//println('xxxxxxxxxxx:');
