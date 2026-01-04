// form表单
import type { PlusColumn } from "plus-pro-components";
import { dronesDeviceGetSelectOptions } from "@/api/dronesDevice";
import { dronesRouteLibraryGetSelectOption } from "@/api/dronesRouteLibrary";

export function useCollectorBusDevForm() {
  const columnsForm: PlusColumn[] = [
    {
      label: "任务名称",
      prop: "taskName",
      valueType: "copy"
    },
    {
      label: "设备信息",
      prop: "deviceId",
      valueType: "select",
      options: async () => {
        return new Promise(resolve => {
          resolve(dronesDeviceGetSelectOptions().then(res => res.data));
        });
      }
    },
    {
      label: "航线信息",
      prop: "routeId",
      valueType: "select",
      options: async () => {
        return new Promise(resolve => {
          resolve(dronesRouteLibraryGetSelectOption().then(res => res.data));
        });
      }
    },
    {
      label: "任务描述",
      prop: "taskDescription",
      valueType: "textarea"
    }
  ];

  const columnsQueryForm: PlusColumn[] = [
    {
      label: "任务名称",
      prop: "taskName",
      valueType: "copy",
      colProps: {
        span: 4
      },
      formItemProps: {
        style: {
          width: "100%"
        }
      }
    },
    {
      label: "设备ID",
      prop: "deviceIdString",
      valueType: "copy",
      colProps: {
        span: 4
      },
      formItemProps: {
        style: {
          width: "100%"
        }
      }
    },
    {
      label: "航线名称",
      prop: "routeName",
      valueType: "copy",
      colProps: {
        span: 5
      }
    },
    {
      label: "任务状态",
      prop: "taskStatus",
      valueType: "select",
      options: [
        {
          label: "未开始",
          value: "未开始"
        },
        {
          label: "进行中",
          value: "进行中"
        },
        {
          label: "已完成",
          value: "已完成"
        }
      ],
      colProps: {
        span: 5
      },
      formItemProps: {
        style: {
          width: "230px"
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
