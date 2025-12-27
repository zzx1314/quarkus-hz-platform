<template>
  <div id="messageCompBox" class="container-message">
    <template v-if="message.length">
      <div
        v-for="(item, index) in message"
        :key="`message_${index}`"
        class="box-item"
      >
        <div
          v-if="item.role === 'assistant' || item.content"
          :class="[
            'message-item',
            item.role === 'assistant'
              ? 'message-item--assistant'
              : 'message-item--user'
          ]"
        >
          <el-avatar
            v-if="item.role === 'assistant'"
            class="message-item__avatar"
          >
            <deepseek />
          </el-avatar>
          <div v-else />
          <div
            :class="[
              'message-item__content',
              item.role === 'assistant'
                ? 'message-item__content--left'
                : 'message-item__content--right'
            ]"
          >
            <div class="message-item__text">
              <div v-if="item.messageType === 'file'">
                <div>
                  <div class="flex gap-x-2 text-3xl items-center">
                    <vscode-icons-file-type-word />
                    <div style="color: #000; font-size: 14px">
                      {{ item.fileName }}
                    </div>
                  </div>
                  <MdPreview
                    :id="id"
                    :modelValue="item.content || '思考中...'"
                  />
                </div>
              </div>
              <div v-else>
                <MdPreview :id="id" :modelValue="item.content || '思考中...'" />
              </div>
            </div>
          </div>
          <el-avatar
            v-if="item.role !== 'assistant'"
            class="message-item__avatar"
          >
            <person />
          </el-avatar>
          <div v-else />
        </div>
      </div>
    </template>
    <template v-else>
      <div class="empty-box">
        <el-empty description="暂无对话信息" />
      </div>
    </template>
  </div>
</template>

<script setup>
import { nextTick } from "vue";
import { MdPreview } from "md-editor-v3";
import VscodeIconsFileTypeWord from "~icons/vscode-icons/file-type-word";
import "md-editor-v3/lib/preview.css";
import deepseek from "@/assets/svg/deepseek.svg?component";
import person from "@/assets/svg/person.svg?component";
const id = "preview-only";

const props = defineProps({
  message: {
    type: Array,
    default: () => []
  }
});

const scrollBottom = () => {
  nextTick(() => {
    const div = document.getElementById("messageCompBox");
    div.scrollTop = div.scrollHeight - div.clientHeight;
  });
};

defineExpose({
  scrollBottom
});
</script>

<style scoped lang="scss">
.container-message {
  width: 100%;
  height: 100%;
  overflow: auto;
}

.empty-box {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.box-item {
  margin-bottom: 12px;
}

.message-item {
  display: grid;
  column-gap: 8px;

  &--user {
    grid-template-columns: 0% auto 40px;
    justify-content: end;
  }

  &--assistant {
    grid-template-columns: 40px auto 1%;
    justify-content: start;
  }

  &__avatar {
    width: 36px;
    height: 36px;
    background-color: #ffffff;
    padding: 4px;
  }

  :deep(.md-editor-preview) {
    font-size: 15px;
  }

  &__content {
    border: 1px solid #409eff;
    background-color: #ffffff;
    position: relative;
    border-radius: 8px;

    &--left::before,
    &--right::before {
      content: "";
      width: 0;
      height: 0;
      position: absolute;
      border: 5px solid transparent;
      top: 15px;
    }

    &--left::before {
      border-right-color: #409eff;
      left: -9px;
    }

    &--right::before {
      border-left-color: #409eff;
      right: -9px;
    }
  }

  &__text {
    padding: 0rem 12px;
    color: #fff;
    position: relative;
    font-size: 0.875rem;
    line-height: 1.4;

    :deep(p) {
      margin: 0.5rem 0;
    }

    :deep(pre) {
      margin: 0 0;
      font-size: 0.8125rem;
      max-width: 100%;
      overflow-x: auto;
      white-space: pre-wrap;
      word-wrap: break-word;
    }

    :deep(code) {
      font-size: 0.8125rem;
      max-width: 100%;
      overflow-x: auto;
      white-space: pre-wrap;
      word-wrap: break-word;
    }
  }
}
</style>
