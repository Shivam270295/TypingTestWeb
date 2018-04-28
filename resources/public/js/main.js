var inputPara = document.getElementById('inputPara');
var testParaHtml = document.getElementById('testPara');
var timerHtml = document.getElementById('timer');
var testPara = testParaHtml.innerHTML;

var timerStarted = false;

var lineArray = [];

function printString(){
	console.log("Hello")
	//testParaHtml.innerHTML = "<font color='#bd0000'>" + testPara + "</font>";
	var line = 3;
	var lineHeight = parseInt(window.getComputedStyle(testParaHtml).getPropertyValue("line-height"),10);// testParaHtml.clientHeight / testParaHtml.rows;
	var fontHeight = parseInt(window.getComputedStyle(testParaHtml).getPropertyValue("font-size"),10);
  var jump = (line - 1) * lineHeight;
  splitPara();
}


inputPara.addEventListener('input', function() {
	evalInput();
});

function evalInput(){
	var wrongWords = [];
	var occ = 0;
	var color = "#ff0000";
	if(inputPara.value.length>0 && !timerStarted){
		timerStarted = true;
		startTimer();
	}
  var testWords = testPara.split(" ");
  var myWords = inputPara.value.split(" ");
  //console.log(myWords);
  var text = "<font color='#bdbdbd'>";
  for(occ = 0; occ < myWords.length-1; occ++){
  	//console.log(myWords[i]);
  	text += testWords[occ] + " ";
  	if(testWords[occ]!=myWords[occ]){
  		wrongWords.push([testWords[occ],myWords[occ]]);
  	}
  }
  if(testWords[occ].indexOf(myWords[occ])==0){
      color = "#4caf50";
  }
  text += "</font><font color='"+color+"'>"+testWords[occ]+"</font> ";
  occ++;
  var line = 0;
  while(occ>lineArray[line][0] && line<lineArray.length){
  	line++;
  }
  for(occ; occ<testWords.length;occ++){
      text+=testWords[occ] + " ";
  }
  testParaHtml.innerHTML = text;
  if(line>2){
    var lineHeight = parseInt(window.getComputedStyle(testParaHtml).getPropertyValue("line-height"),10);// testParaHtml.clientHeight / testParaHtml.rows;
    var jump = (line - 3) * lineHeight;
    testParaHtml.scrollTop = jump;
  }
  return {wrongWords: wrongWords, totalCount: (myWords.length-1)};
}

function startTimer(){
	var i = 1;
	var x = setInterval(function() {
    var distance = 60-i;
    i++;   
    timerHtml.innerHTML = distance;
    // If the count down is over, write some text 
    if (distance < 0) {
        clearInterval(x);
        timerHtml.innerHTML = "60";
        var obj = evalInput();
        showResult(obj.wrongWords, obj.totalCount);
        testParaHtml.innerHTML = testPara;
        inputPara.value = '';
        testParaHtml.scrollTop = 0;
    }
	}, 1000);
}

function splitPara(){
	var $cont = $('#testPara');
	var text_arr = testPara.split(' ');
	for (i = 0; i < text_arr.length; i++) {
	    text_arr[i] = '<span>' + text_arr[i] + ' </span>';
	}
	$cont.html(text_arr.join(''));
	$wordSpans = $cont.find('span');
	var lineIndex = 0,
	    lineStart = true,
	    lineEnd = false
	$wordSpans.each(function(idx) {
	    var pos = $(this).position();
	    var top = pos.top;
	    if (lineStart) {
	        lineArray[lineIndex] = [idx];
	        lineStart = false;
	    } else {
	        var $next = $(this).next();
	        if ($next.length) {
	            if ($next.position().top > top) {
	                lineArray[lineIndex].push(idx);
	                lineIndex++;
	                lineStart = true
	            }
	        } else {
	            lineArray[lineIndex].push(idx);
	        }
	    }
	});
	for (i = 0; i < lineArray.length; i++) {
		var start = lineArray[i][0],
		    end = lineArray[i][1] + 1;
		/* no end value pushed to array if only one word last line*/
		if (!end) {
		    $wordSpans.eq(start).wrap('<span class="line_wrap">')
		} else {
		    $wordSpans.slice(start, end).wrapAll('<span class="line_wrap">');
		}
	}
}

splitPara();

function showResult(wrongWords, totalCount){
	var score = totalCount*10 - wrongWords.length*15;
	var message = "Your score is " + score + ".\n";
	for(var i = 0; i < wrongWords.length; i++){
		if(i==0){
			message+="\nWorng words:";
		}
		message+="\n  '"+wrongWords[i][0]+"' spelled as '"+wrongWords[i][1]+"'";
	}

  /*testParaHtml.innerHTML = testPara;
  inputPara.innerHTML = "";*/
	alert(message);
}

