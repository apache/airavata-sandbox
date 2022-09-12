<template>
  <h1>hi</h1>
</template>

<script>
import store from "airavata-custos-portal/src/lib/store";

export default {
  name: "CallBack",
  components: {},
  store: store,
  data() {
    return {
      errors: []
    }
  },
  methods: {
    async authenticate() {
      this.errors = [];

      let code = this.$route.query.code
      let params = {code: code};

      try {
        await this.$store.dispatch('auth/authenticateUsingCode', params)
      } catch (e) {
        this.errors.push({
          variant: "danger",
          title: "Authentication Error",
          description: "Please contact the system administrator",
          source: e
        });
      }

    }
  },
  async mounted() {
    await this.authenticate();
    await this.$router.push('/home');
  }
}
</script>

<style scoped>

</style>

