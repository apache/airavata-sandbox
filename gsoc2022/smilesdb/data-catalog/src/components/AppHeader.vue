<template>
  <nav class="navbar navbar-expand-lg bg-dark">
    <div class="container-fluid">
      <a class="navbar-brand">
        <router-link to="/home">SEAGrid Home</router-link>
        <button class="btn btn-sm" type="button">
          <router-link to="/search">
            Data Catalog
          </router-link>
        </button>
      </a>
    </div>
  </nav>
</template>

<script>
export default {
  name: "AppHeader"
}
</script>

<style scoped>
a {
  padding-left: 17%;
  color: #f8f9fa;
}

a:hover {
  color: rgba(117, 114, 114, 0.98);
  text-decoration: none;
}


.btn {
 all:unset;
  font-size: 15px;
  padding-left: 2%;
}

</style>