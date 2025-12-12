// form表单
import type { PlusColumn } from "plus-pro-components";

export function useCollectorBusDevForm() {
  const columnsForm: PlusColumn[] = [
    {
      label: "知识库名称",
      prop: "knowledgeBaseName",
      valueType: "copy"
    },
    {
      label: "知识库类型",
      prop: "knowledgeBaseType",
      valueType: "copy"
    },
    {
      label: "知识库描述",
      prop: "knowledgeBaseDesc",
      valueType: "copy"
    }
  ];

  const columnsQueryForm: PlusColumn[] = [
    {
      label: "知识库名称",
      prop: "knowledgeBaseName",
      valueType: "copy",
      colProps: {
        span: 5
      },
      formItemProps: {
        labelWidth: "100px",
        style: {
          width: "100%"
        }
      }
    },
    {
      label: "知识库类型",
      prop: "knowledgeBaseType",
      valueType: "copy",
      colProps: {
        span: 5
      },
      formItemProps: {
        labelWidth: "100px",
        style: {
          width: "100%"
        }
      }
    },
    {
      label: "知识库描述",
      prop: "knowledgeBaseDesc",
      valueType: "copy",
      colProps: {
        span: 5
      },
      formItemProps: {
        labelWidth: "100px",
        style: {
          width: "100%"
        }
      }
    },
    {
      label: "开始时间",
      prop: "beginTime",
      valueType: "date-picker",
      type: "date",
      formItemProps: {
        style: {
          width: "250px"
        }
      },
      fieldProps: {
        type: "date",
        valueFormat: "YYYY-MM-DD HH:mm:ss"
      },
      colProps: {
        span: 5
      }
    },
    {
      label: "结束时间",
      prop: "endTime",
      valueType: "date-picker",
      formItemProps: {
        style: {
          width: "250px"
        }
      },
      fieldProps: {
        type: "date",
        valueFormat: "YYYY-MM-DD"
      },
      colProps: {
        span: 5
      }
    }
  ];
  return {
    columnsForm,
    columnsQueryForm
  };
}
