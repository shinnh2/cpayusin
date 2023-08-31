import type { Meta, StoryObj } from "@storybook/react";

import Input from "./../components/Input";
import type { InputProps } from "./../components/Input";

// More on how to set up stories at: https://storybook.js.org/docs/react/writing-stories/introduction#default-export
const meta = {
	title: "Component/Input",
	component: Input,
	parameters: {
		// Optional parameter to center the component in the Canvas. More info: https://storybook.js.org/docs/react/configure/story-layout
		layout: "centered",
	},
	// This component will have an automatically generated Autodocs entry: https://storybook.js.org/docs/react/writing-docs/autodocs
	tags: ["autodocs"],
	// More on argTypes: https://storybook.js.org/docs/react/api/argtypes
	argTypes: {
		InputLabel: {
			control: "text",
			defaultValue: "테스트",
			description: "원하는 텍스트를 입력해보세요",
		},
		isLabel: {
			control: {
				type: "boolean", //label의 유무를 나타냅니다.
			},
		},
		inputAttr: {},
		errorMsg: {
			control: "text",
			defaultValue: "에러 메시지",
			description: "에러 메시지가 표시됩니다.",
		},
	},
} satisfies Meta<InputProps>;

export default meta;
// type Story = StoryObj<typeof meta>;

// More on writing stories with args: https://storybook.js.org/docs/react/writing-stories/args
export const InputText: Meta<InputProps> = {
	args: {
		InputLabel: "레이블명",
		isLabel: true,
		inputAttr: { type: "text", placeholder: "입력하세요" },
		errorMsg: "에러 메시지입니다.",
	},
};
