
function makeThing() {
	var a = document.createElement("a");
	a.innerHTML = "Thing  ";
	a.style.fontFamily = "Tahoma, Geneva, sans-serif";
	a.classList.add("thing_link");
	a.href = "https://www.google.com/search?rlz=1C1CHBD_enUS875US875&ei=X_koX8izOfOoytMPhpqIoAo&q=thing&oq=thing&gs_lcp=CgZwc3ktYWIQAzIHCAAQsQMQQzIHCAAQsQMQQzIECAAQQzINCC4QsQMQxwEQowIQQzICCAAyBwgAELEDEEMyBAgAEEMyBwgAELEDEEMyBAgAEEMyBQgAELEDOg4ILhCxAxDHARCjAhCTAjoICAAQsQMQgwE6CwguELEDEMcBEKMCOggILhDHARCjAjoICC4QsQMQgwE6BAguEEM6AgguOgcILhBDEJMCOggILhDHARCvAToFCC4QsQM6CwguELEDEMcBEK8BOgcILhCxAxBDUN0NWKMRYN8VaABwAXgAgAG9AYgBowSSAQMzLjKYAQCgAQGqAQdnd3Mtd2l6wAEB&sclient=psy-ab&ved=0ahUKEwiI36C574DrAhVzlHIEHQYNAqQQ4dUDCAw&uact=5";
	document.getElementById("things").appendChild(a);
	updateLinks(a);
	document.getElementById("make_thing").style.color = getRandomColor();
	document.getElementById("make_thing").style.backgroundColor = getRandomColor();
}



window.onload = () =>{
	console.log("Page loaded");
}

make_thing.onclick = () => {
	makeThing();
}

let hovered = false;

no_click.onmouseover = () => {
	if(!hovered){
		alert("I'm warning you. You better not click it!");
		hovered = true;
	}
}

let numClicks = 0;

no_click.onclick = () => {
	numClicks++;
	switch(numClicks){
		case 1:
			alert("You shouldn't have done that. Don't do it again");
			break;
		case 2:
			alert("That was your second warning.");
			break;
		case 3:
			alert("This won't end well for you.");
			break;
		case 4:
			alert("Please stop");
			break;
		case 5:
			alert("You're only hurting yourself.");
			break;
		case 6:
			alert("You should really start listening to me.");
			break;
		case 7:
			alert("Last chance.");
			break;
		default:
			window.open("https://www.youtube.com/watch?v=dQw4w9WgXcQ");
			no_click.setAttribute("value", "I warned you not to click me");
	}
}


function updateLinks(a){

    a.onmouseover = function()
    {
        this.style.color = getRandomColor();
    }

}

function getRandomColor() {
  var letters = '0123456789ABCDEF';
  var color = '#';
  for (var i = 0; i < 6; i++) {
    color += letters[Math.floor(Math.random() * 16)];
  }
  return color;
}

