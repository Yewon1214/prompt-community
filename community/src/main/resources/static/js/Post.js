class Post{
    constructor() {
        this.deleteFileIds = [];
        this.fileNum = 0;
        this.deleteFileIdInput = document.getElementById("delete-file-ids-input");
        this.fileInput = document.getElementById("file-input");

        this.fileDeleteBtn = document.getElementsByClassName("file-delete-btn");
        if(this.fileDeleteBtn){
            this.fileNum = this.fileDeleteBtn.length;
            for(let i=0; i<this.fileNum; i++){
                this.fileDeleteBtn[i].addEventListener("click", this.removeAttachment)
            }
        }

    };

    removeAttachment = (e) =>{
        let attachmentFile = e.target.parentNode;
        this.deleteFileIds.push(Number(attachmentFile.getAttribute("data-id")));
        attachmentFile.remove();
        this.deleteFileIdInput.value = this.deleteFileIds;
        console.log(this.deleteFileIds);
        this.fileNum--;
    };


}

