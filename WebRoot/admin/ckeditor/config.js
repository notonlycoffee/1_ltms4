/*
Copyright (c) 2003-2011, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

CKEDITOR.editorConfig = function( config )
{
	// Define changes to default configuration here. For example:
	// config.language = 'fr';
	// config.uiColor = '#AADC6E';
	    config.language = 'zh-cn' ;
    	config.skin = 'kama'; 
    	config.image_previewText = "预览图片 ";
      config.font_names = '宋体/宋体;黑体/黑体;仿宋/仿宋_GB2312;楷体/楷体_GB2312;隶书/隶书;幼圆/幼圆;微软雅黑/微软雅黑;' + config.font_names;
  config.pasteFromWordRemoveStyle = false;
  config.forcePasteAsPlainText =false; 
  config.font_defaultLabel = '宋体';
config.keystrokes = [
       [ CKEDITOR.ALT + 121 /*F10*/, 'toolbarFocus' ],  //获取焦点
        [ CKEDITOR.ALT + 122 /*F11*/, 'elementsPathFocus' ],  //元素焦点

       [ CKEDITOR.SHIFT + 121 /*F10*/, 'contextMenu' ],  //文本菜单

       [ CKEDITOR.CTRL + 90 /*Z*/, 'undo' ],  //撤销
        [ CKEDITOR.CTRL + 89 /*Y*/, 'redo' ],  //重做
        [ CKEDITOR.CTRL + CKEDITOR.SHIFT + 90 /*Z*/, 'redo' ]  //
] 
config.toolbar = 'Full';
config.toolbar_Full = [
       ['Source','-','Save','NewPage','Preview','-','Templates'],
       ['Cut','Copy','Paste','PasteText','PasteFromWord','-','Print', 'SpellChecker', 'Scayt'],
       ['Undo','Redo','-','Find','Replace','-','SelectAll','RemoveFormat'],
       ['Form', 'Checkbox', 'Radio', 'TextField', 'Textarea', 'Select', 'Button', 'ImageButton', 'HiddenField'],
       '/',
       ['Bold','Italic','Underline','Strike','-','Subscript','Superscript'],
        ['NumberedList','BulletedList','-','Outdent','Indent','Blockquote'],
        ['JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'],
        ['Link','Unlink','Anchor'],
       ['Image','Flash','Table','HorizontalRule','Smiley','SpecialChar','PageBreak'],
       '/',
        ['Styles','Format','Font','FontSize'],
        ['TextColor','BGColor']
    ];
};
