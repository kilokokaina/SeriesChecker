tinymce.init({
	selector: 'textarea.tinymce',
	plugins: 'advlist autolink link image lists charmap print preview hr anchor pagebreak searchreplace wordcount visualblocks visualchars code fullscreen insertdatetime media nonbreakingsave table contextmenu directionality emoticons template paste textcolor',
	toolbar: 'insertfile undo redo styleselect bold italic alignleft aligncenter alignright alignjustify bullist numlist outdent indent link image print preview media fullpage forecolor backcolor emoticons',
	style_formats: [
		{title: 'Imagem responsiva', selector: 'img', styles: {
			'width' : '100%',
			'height' : '100%'
		}}
	]
});