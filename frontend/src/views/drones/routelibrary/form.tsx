// form表单
import type { PlusColumn } from "plus-pro-components";

export function useCollectorBusDevForm() {
  const columnsForm: PlusColumn[] = [
    {
      label: "路线名称",
      prop: "routeName",
      valueType: "copy"
    },
    {
      label: "路线类型",
      prop: "routeType",
      valueType: "select",
      options: [
        {
          label: "离线地图",
          value: "离线地图"
        },
        {
          label: "在线地图",
          value: "在线地图"
        }
      ]
    },
    {
      label: "路线描述",
      prop: "routeDescription",
      valueType: "textarea"
    }
  ];

  const columnsQueryForm: PlusColumn[] = [
    {
      label: "路线名称",
      prop: "routeName",
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
