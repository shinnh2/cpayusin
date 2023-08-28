import React from "react";
import { useRef } from "react";
import { Editor } from "@toast-ui/react-editor";
import "@toast-ui/editor/dist/toastui-editor.css";
import "@toast-ui/editor/dist/i18n/ko-kr";

function EditorUnit() {
	const editorRef = useRef<Editor>(null);

	const submitHandler = () => {
		const data: string = editorRef.current!.getInstance().getHTML();
		console.log(data);
	};

	return (
		<div className="editor_unit">
			<header>JBaccount HOME</header>
			<Editor
				initialValue="내용을 입력하세요"
				previewStyle="vertical"
				height="600px"
				language="ko-KR"
				initialEditType="wysiwyg"
				useCommandShortcut={true}
				ref={editorRef}
			/>
			<button className="submit_btn" onClick={submitHandler}>
				등록
			</button>
		</div>
	);
}

export default EditorUnit;
