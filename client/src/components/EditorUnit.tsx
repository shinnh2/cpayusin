import React from "react";
import { useRef } from "react";
import { Editor } from "@toast-ui/react-editor";
import "@toast-ui/editor/dist/toastui-editor.css";
import "@toast-ui/editor/dist/i18n/ko-kr";
import Button from "./Button";

const EditorUnit = React.forwardRef((props, ref) => {
	const editorRef = useRef<Editor>(null);

	//Editor의 내용을 가져오는 함수
	const getInstance = () => {
		return editorRef.current?.getInstance();
	};

	React.useImperativeHandle(ref, () => ({
		getInstance,
	}));

	return (
		<div className="editor_unit">
			<Editor
				initialValue=" "
				previewStyle="vertical"
				height="600px"
				language="ko-KR"
				initialEditType="wysiwyg"
				useCommandShortcut={true}
				ref={editorRef}
			/>
		</div>
	);
});

export default EditorUnit;
