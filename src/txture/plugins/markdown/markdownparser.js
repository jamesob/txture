<script src="/js/showdown.js" type="text/javascript"></script>
<script>
var postbodies = document.getElementsByTagName('post-body'),
	converter = new Showdown.converter();

if(postbodies) {
	for (var i = 0; i < postbodies.length; i++) {
		var converted = converter.makeHtml(postbodies[i].value);
		postbodies[i].innerHTML = converted;
	}
}
</script>
