console.log("board_detail.js in")
console.log(bnoVal);
document.getElementById('cmtAddBtn').addEventListener('click',()=>{
    let cmtText=document.getElementById('cmtText').value;
    const cmtWriter = document.getElementById('cmtWriter').value;
    if(cmtText ==null || cmtText ==''){
        alert('댓글을 입력해주세요.')
        return false;
    }else{
        // 댓글등록
        let cmtData = {
            bno: bnoVal,
            writer:cmtWriter,
            content: cmtText
        };
        // 댓글 비동기로 전송
        postCommentToServer(cmtData).then(result=>{
            console.log(result);
			if(result ='1'){
				alert('댓글 등록 성공~');
				document.getElementById('cmtText').value ='';
			}
			
			//댓글출력
            printCommentList(bnoVal);

			
        })

    }
})

async function postCommentToServer(cmtData){
    try {
        //method , headers, body
        const url = "/cmt/post";
        const config ={
            method : 'post',
            headers : {
                'Content-Type' : 'application/json; charset-utf-8'
            },
            body: JSON.stringify(cmtData)

        };

        const resp = await fetch(url,config);
        const result = await resp.text(); //isOk 값을 리턴
        return result;


    }catch(error){
        console.log(error);
    }
}

function spreadCommentList(result){ //result 는 댓글 리스트
    console.log(result);
    let div = document.getElementById('commentLine');
    div.innerText =''; //원래 만들어두었던 구조 지우기
    for(let i =0; i<result.length ; i++){
        let html = `<div>`;
        html += `<div>${result[i].cno},${result[i].bno},${result[i].writer},${result[i].regdate}</div>`;
        html += `<div>`;
        html += `<input type="text" class="cmtText" value="${result[i].content}">`;

        if(id ===result[i].writer){

            html += `<button type="button" data-cno=${result[i].cno} class="cmtModBtn" >수정 </button>`;
            html += `<button type="button" data-cno=${result[i].cno} class="cmtDelBtn" >삭제 </button> <br>`;
        }

        html += `</div>`;
        html += `</div><hr>`;

        div.innerHTML +=html; // 각 댓글을 누적하여 담기

    
    }
}

    async function getCommentListFromServer(bno){
        try{
            const resp = await fetch("/cmt/list?bno="+bno);
            const result = await resp.json(); //'[{...}{...}]'
            return result;
        }catch(error){
            console.log(error);
        }
    }

    function printCommentList(bno){
        getCommentListFromServer(bno).then(result=>{
            console.log(result);
            if(result.length > 0){
                spreadCommentList(result);
            }else{
                let div = document.getElementById('commentLine');
                div.innerHTML =`<div>comment 가 없습니다.</div>`;
            }
        })
    }

    //수정: cno ,content => result isOk => post 처럼

    async function updateCommentFromServer(cmtData){
        try{
            const url ="/cmt/modify";
            const config ={
                method: 'post',
                headers : {
                    'Content-Type' : 'application/json; charset=utf-8'
                },
                body : JSON.stringify(cmtData)
            }

            const resp = await fetch(url,config);
            const result =await resp.text();
            return result;
        }catch(error){
            console.log(error);
        }
    }
    // 삭제 : cno => list처럼
    async function removeCommentFromServer(cnoVal){
        try {
            const resp = await fetch("/cmt/remove?cno="+cnoVal);
            const result = resp.text();
            return result;
        } catch (error) {
            console.log(error);
        }
    }

    document.addEventListener('click',(e)=>{
        console.log(e.target);

        if(e.target.classList.contains('cmtDelBtn')){
            let cnoVal =e.target.dataset.cno; // 데이터값 추출
            console.log(cnoVal);
            removeCommentFromServer(cnoVal).then(result =>{
                if(result ==='1'){
                    alert('댓글 삭제 성공!');
                    printCommentList(bnoVal);
                }
            })
        }

    if(e.target.classList.contains('cmtModBtn')){
        let cnoVal = e.target.dataset.cno;
        console.log(cnoVal);
        let div = e.target.closest('div');
        console.log(div);
        let cmtText = div.querySelector('.cmtText').value;
        console.log(cmtText);
        let cmtData ={
            cno:cnoVal,
            content:cmtText
        }
        updateCommentFromServer(cmtData).then(result =>{
            if(result ==='1'){
                alert('댓글수정성공!!!');
                printCommentList(bnoVal);
            }
        })
    }
    })


/*
<div id="commentLine">
	<div>
		<div>cno,bno,wirter,regdate</div>
		<div>
			<button>수정</button><button>삭제</button> <br>
			<input type="text" value="content">
		</div>
	</div>
</div>
 */ 