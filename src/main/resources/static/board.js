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
  
		//button.addEventListener('click', function() {
        //    const boardId = this.dataset.id;	//ボタンのth:data-id属性から対応する投稿のIDを取得
		
		button.addEventListener('click', (event) => { // アロー関数に変更
		    //const boardId = button.dataset.id; // this.dataset.id を button.dataset.id に変更
			// event.currentTargetがクリックされたボタンを正確に参照
			const clickedButton = event.currentTarget;
			const boardId = clickedButton.dataset.id;

			          
            if (confirm(`本当に削除しますか？\n\n記事ID: ${boardId}`)) {            //  confirmメッセージをテンプレートリテラルで記述
                fetch(`/api/board/${boardId}`, {	//サーバーに非同期でリクエストを送信
                    method: 'DELETE',		//REST API
					// CSRFトークンをリクエストヘッダーに追加
					headers: {
					  [csrfHeader]: csrfToken // 最初の定数を使用
					}
                })
                .then(response => {
                    if (response.ok) {
						console.log("サーバからのレスポンスはOK");
                        // 削除成功後、ページをリロードして一覧を更新ー＞ajaxにするため、やめる
                        //window.location.reload();
						//const postElement = this.closest('.post-item');
						// button.closest() に変更して、親要素を正確に特定→NG
						//const postElement = button.closest('.post-item');
						// クリックされたボタンの親要素をDOMから削除
						const postElement = clickedButton.closest('.post-item');
						
						// postElementが取得できたか確認する
						console.log('取得した投稿要素:', postElement);
						
						        if (postElement) {
						              postElement.remove();
						         }
					} else if (response.status === 403) {
					       // 権限がない場合のエラーを明記
					  alert('この投稿を削除する権限がありません。');
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
		
		/**
		 * 記事投稿
		 * フォームの入力値はまず非同期でサーバーに送信されてバリデーションが行われ、
		 * エラーがあればページをリロードせずにエラーメッセージが表示。
		 * バリデーションに成功した場合のみ、本来のフォーム送信が実行され、投稿が完了
		 */
		
		//const postForm = document.querySelector('form');
		const postForm = document.getElementById('postForm');
		postForm.addEventListener('submit', function(event) {
			console.log('Submit event triggered.');
		    event.preventDefault(); // フォームのデフォルト送信をキャンセル
			console.log('Prevent default called');
		    
		    const boardText = document.getElementById('text').value;
			
			const postData = {
			    text: boardText
			};
			
			// デバッグログの追加
			 console.log('取得したテキストフィールドの値:', boardText);
			 console.log('値の長さ:', boardText.length);
			 console.log('JSONデータ:', postData);

		    // エラーメッセージをクリア
		    document.querySelectorAll('.is-invalid').forEach(el => el.classList.remove('is-invalid'));
		    document.querySelectorAll('.invalid-feedback').forEach(el => el.textContent = '');


		    
		    // バリデーションリクエスト
		    fetch(`/api/board/validate`, {
		        method: 'POST',
		        headers: {
		            'Content-Type': 'application/json',
		            [csrfHeader]: csrfToken // 最初の定数を使用
		        },
		        body: JSON.stringify(postData)
		    })
			
			
			.then(response => {
			    if (!response.ok) {
					// バリデーション失敗、エラー情報を取得して処理
					
					if (response.status === 400) {

			        	return response.json().then(errorData => {
			            	for (const field in errorData) {
			                	// エラーが発生したフィールドIDに対応する要素を取得
			                	const inputField = document.getElementById(field);
			                	if (inputField) {
			                    	inputField.classList.add('is-invalid');//赤い枠線を追加

			               /*     // 親要素（form-group）内でエラーメッセージのdivを探す
			                    const formGroup = inputField.closest('.form-group');
			                    if (formGroup) {
			                        const errorDiv = formGroup.querySelector('.invalid-feedback');
			                        if (errorDiv) {
			                            errorDiv.textContent = errorData[field];
			                        }
			                    }*/
									// nextElementSiblingで直後の兄弟要素を探す
									const errorDiv = inputField.nextElementSibling;
									if (errorDiv && errorDiv.classList.contains('invalid-feedback')) {
									// errorDiv.textContent = errorData[field];エラー2つ以上だと見にくい
									const formattedMessage = errorData[field].split(',').join('<br>');
									 errorDiv.innerHTML = formattedMessage;
									
									} else {
									// 予期せぬエラーの場合、エラーメッセージをログに出力
									console.error('エラーメッセージ表示要素が見つかりませんでした。', errorDiv);
							 	}
			                }
			            }
			

		                // バリデーションエラーの場合はここで処理を終了し、エラーを伝播させない　catchブロックへ
		                return Promise.reject('Validation failed');
		            });
					} else {
					    // その他の理由で失敗
						
					    return Promise.reject('投稿に失敗しました。');
					}
		        }
		        // バリデーション成功、次のthenブロックにレスポンスを渡す
		        return response;
		    })
		    .then(() => {
		        // バリデーション成功時の処理　実際の投稿リクエストを送信
		        return fetch(`/api/board/post`, {
		            method: 'POST',
		            headers: {
		                'Content-Type': 'application/json',
		                [csrfHeader]: csrfToken
		            },
		            body: JSON.stringify(postData)
		        });
		    })
		    .then(postResponse => {
		        if (postResponse && postResponse.ok) {
		            window.location.reload(); // 投稿成功
		        } else {
		            alert('投稿に失敗しました。');
		        }
		    })
		    .catch(error => {//ここでerror変数を定義
		        // バリデーション失敗時には何もしない
		        if (error !== 'Validation failed') {
		            console.error('通信中にエラーが発生しました:', error);
		            alert('処理中にエラーが発生しました。');
		        }
	 		 });//catch-then
		
		 });//postForm.addEventListener
				 
	
			
	
	});//addEventListener
	
	
					 /**
					  * 以下はバリデーション/RESTを使用しない場合なので無視
					  */			 
					 

	/*			     // CSRFトークンとヘッダー名を取得
				     //const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
				     //const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');
				     
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
	    		});//postForm.addEventListener */