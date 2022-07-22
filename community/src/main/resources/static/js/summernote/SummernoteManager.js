class SummernoteManager{
    constructor(content){
        var self = this;
        this.options = {
            placeholder: '내용을 입력하세요',
            lang: "ko-KR",
            tabsize: 2,
            height: 400,
            toolbar: [
                ['fontname', ['fontname']],
                ['fontsize', ['fontsize']],
                ['style', ['bold', 'italic', 'underline', 'strikethrough', 'clear']],
                ['color', ['color']],
                ['para', ['ul', 'ol', 'paragraph']],
                ['height', ['height']],
                ['insert', ['picture', 'link']]
            ],
            fontNames: ['Arial', 'Arial Black', 'Comic Sans MS', 'Courier New','맑은 고딕','궁서','굴림체','굴림','돋음체','바탕체', '나눔바른고딕', '나눔고딕', '나눔손글씨체', '한돋움체', '나눔명조', '나눔명조에코', '마루부리'],
            fontSizes: ['8','9','10','11','12','14','16','18','20','22','24','28','30','36','50','72'],
            callbacks: {
                onImageUpload: function (files){
                    self.sendImg(files, $(this));
                },
                onPaste: function (e){
                    var clipboardData = e.originalEvent.clipboardData;
                    if(clipboardData && clipboardData.items && clipboardData.items.length){
                        var item = clipboardData.items[0];
                        if(item.kind === 'file' && item.type.indexOf('image/' !== -1)){
                            e.preventDefault();
                        }
                    }
                }
            }
        };
        this.summernote = $('#summernote').summernote(this.options);

    }


    sendImg = (files, editor) => {
        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");
        var data = new FormData();

        for(var i=0; i<files.length; i++){
            data.append("multipartFiles", files[i]);
        }
        console.log(data.get("multipartFiles"));
        $.ajax({
            data: data,
            beforeSend: function(xhr){
                xhr.setRequestHeader(header, token);
            },
            type: 'POST',
            url: '/files/summernote/',
            enctype: 'multipart/form-data',
            cache: false,
            contentType: false,
            processData: false,
            success: function(data){
                for(var i=0; i<data.length; i++){
                    var image = $('<img>').attr({
                        src: '/upload'+data[i].relativePath,
                        class: 'img-fluid'
                    });
                    editor.summernote('insertNode', image[0]);
                    // editor.summernote('insertImage', '/upload' + data[i].relativePath);
                }
            },
            error: function(jqXHR, textStatus, errorThrown){
                console.log(errorThrown);
            }
        });

    }
}