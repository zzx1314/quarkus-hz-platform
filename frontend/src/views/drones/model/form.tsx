// form表单
import type { PlusColumn } from "plus-pro-components";

export function useCollectorBusDevForm() {
  const columnsForm: PlusColumn[] = [
    {
      label: "文件名称",
      prop: "fileName",
      valueType: "copy"
    },
    {
      label: "备注",
      prop: "remark",
      valueType: "textarea"
    }
  ];

  const columnsQueryForm: PlusColumn[] = [
    {
      label: "模型名称",
      prop: "modelName",
      valueType: "copy",
      colProps: {
        span: 5
      }
    },
    {
      label: "模型类型",
      prop: "modelType",
      valueType: "select",
      options: [
        {
          label: "地图",
          value: "地图"
        },
        {
          label: "视觉",
          value: "视觉"
        }
      ],
      colProps: {
        span: 5
      },
      formItemProps: {
        style: {
          width: "90%"
        }
      }
    },
    {
      label: "文件名称",
      prop: "fileName",
      valueType: "copy",
      colProps: {
        span: 5
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
