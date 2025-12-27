// form表单
import type { PlusColumn } from "plus-pro-components";

export function useCollectorBusDevForm() {
  const columnsForm: PlusColumn[] = [
    {
      label: "任务名称",
      prop: "taskName",
      valueType: "copy"
    },
    {
      label: "模型位置",
      prop: "modelNameOrPath",
      valueType: "copy"
    },
    {
      label: "prompt模板",
      prop: "template",
      valueType: "copy"
    },
    {
      label: "数据集",
      prop: "dataset",
      valueType: "copy"
    },
    {
      label: "微调类型",
      prop: "finetuningType",
      valueType: "copy"
    },
    {
      label: "保存位置 ",
      prop: "outputDir",
      valueType: "copy"
    },
    {
      label: "采样数据 ",
      prop: "maxSamples",
      valueType: "copy"
    },
    {
      label: "验证集比例",
      prop: "valSize",
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
      label: "任务名称",
      prop: "taskName",
      valueType: "copy",
      colProps: {
        span: 5
      }
    },
    {
      label: "模型位置",
      prop: "modelNameOrPath",
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
