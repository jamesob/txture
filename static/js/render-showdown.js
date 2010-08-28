function renderMarkdown() {
	var postbodies = document.getElementsByClassName('post-body'),
		converter = new Showdown.converter();

	if(postbodies) {
		for (var i = 0; i < postbodies.length; i++) {
			var converted = converter.makeHtml(postbodies[i].innerHTML);
			postbodies[i].innerHTML = converted;
		}
	}
}

window.onload=renderMarkdown

