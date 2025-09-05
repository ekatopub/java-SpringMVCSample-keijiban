/**
 * 記事削除
 */


document.addEventListener('DOMContentLoaded', function() {
    const deleteButtons = document.querySelectorAll('.delete-button');	//HTMLのすべての削除ボタンを取得
	
	// メタタグからCSRFトークンとヘッダー名を取得
	const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
	const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

	// 削除ボタンごとにクリックイベントを追加
    deleteButtons.forEach(button => {
        button.addEventListener('click', function() {
            const boardId = this.dataset.id;	//ボタンのth:data-id属性から対応する投稿のIDを取得
            
            if (confirm(`本当に削除しますか？\n\n記事ID: ${boardId}`)) {            //  confirmメッセージをテンプレートリテラルで記述
                fetch(`/api/board/${boardId}`, {	//サーバーに非同期でリクエストを送信
                    method: 'DELETE',		//REST API
					// CSRFトークンをリクエストヘッダーに追加
					headers: {
					  [csrfHeader]: csrfToken
					}
                })
                .then(response => {
                    if (response.ok) {
                        // 削除成功後、ページをリロードして一覧を更新
                        window.location.reload();
                    } else {
						        return response.json().then(errorData => {
						            alert(errorData.message);
						        });
						    }
						})
                .catch(error => {
                    console.error('削除中にエラーが発生しました:', error);
                    alert('削除中にエラーが発生しました。');
                });//catch-then
				}//if	
		    });//addEventListener
		});//forEach
		
		
			// 新規投稿フォームの処理
			 const postForm = document.querySelector('form');
			 postForm.addEventListener('submit', function(event) {
			     event.preventDefault(); // フォームのデフォルト送信をキャンセル
			     
			     const boardText = document.getElementById('text').value;

			     // CSRFトークンとヘッダー名を取得
			     const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
			     const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');
			     
			     const postData = {
			         text: boardText
			     };
			     
			     fetch(`/api/board/post`, {
			         method: 'POST',
			         headers: {
			             'Content-Type': 'application/json',
			             [csrfHeader]: csrfToken
			         },
			         body: JSON.stringify(postData)
			     })
			     .then(response => {
			         if (response.ok) {
			             window.location.reload();
			         } else {
			             alert('投稿に失敗しました。');
			         }
			     })
			     .catch(error => {
			         console.error('投稿中にエラーが発生しました:', error);
			         alert('投稿中にエラーが発生しました。');
				 });//catch-then
    		});//postForm.addEventListener
	
	});//addEventListener