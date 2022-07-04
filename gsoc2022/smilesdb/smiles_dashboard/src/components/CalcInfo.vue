<template>
    <div class="calcinfo_container">
    <div class="calcinfo_content" >
      <h1>CalcInfo</h1>
      <div >SMILES: {{calcinfo.SMILES}}</div>
      <div >nmo: {{calcinfo.nmo}}</div>
      <div >nbeta: {{calcinfo.nbeta}}</div>
      <div >nalpha: {{calcinfo.nalpha}}</div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      calcinfo:{},
    }
  },
  methods: {
    async getData() {
      try {
        // fetch calcinfo
        const response = await this.axios.get('http://localhost:8000/api/calcinfo/');
        // set the data returned as calcinfo
        this.calcinfo = JSON.parse(response.data);
        console.log(this.calcinfo);
      } catch (error) {
        // log the error
        console.log(error);
      }
    },
  },
  created() {
    this.getData();
  }
}
</script>

<style scoped>

</style>