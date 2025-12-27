<template>
  <div>
    <div class="mb-2">
      <el-button type="primary" link :icon="useRenderIcon(Back)" @click="back()"
        >返回</el-button
      >
    </div>
    <chat
      chatType="应用"
      :chat-clear="chatModeType"
      :application-type="applicationType"
      :application-id="Number(applicationId)"
      :application-name="String(applicationName)"
    />
  </div>
</template>
<script setup lang="ts">
import Chat from "@/views/ai/chat/index.vue";
import { useDetail } from "@/views/ai/application/hooks/applicationChatHook";
import { useRenderIcon } from "@/components/ReIcon/src/hooks";
import Back from "~icons/ep/back";
import { useMultiTagsStoreHook } from "@/store/modules/multiTags";
import { useRouter } from "vue-router";

const { initToDetail, getParameter } = useDetail();

initToDetail();
const applicationId = getParameter.applicationId;
const applicationName = getParameter.applicationName;
const chatModeType = Array.isArray(getParameter.chatModeType)
  ? getParameter.chatModeType[0] || ""
  : getParameter.chatModeType || "";
const applicationType = Array.isArray(getParameter.applicationType)
  ? getParameter.applicationType[0] || ""
  : getParameter.applicationType || "";
const router = useRouter();

const back = () => {
  console.log("back");
  useMultiTagsStoreHook().handleTags(
    "splice",
    "/ai/application/applicationChat"
  );
  router.push({ path: "/ai/application/index" });
};
</script>
