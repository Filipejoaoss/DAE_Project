<template>
  <div>
    <br>
    <b-container>
      <h3>Send a Report</h3>
      <br>
      <form @submit.prevent="send">
        <p><b>From: </b>{{username}}</p>
        <br>
        <div class="input-group">
          <span class="input-group-text">Message</span>
          <textarea class="form-control" aria-label="With textarea" v-model="message" name="message"></textarea>
        </div>
        <br>
        <b-button @click.prevent="send">Send</b-button>
        <nuxt-link to="/" class="btn float-right">
          <b-button>Back</b-button>
        </nuxt-link>
      </form>
    </b-container>
  </div>
</template>
<script>
export default {
  layout: 'navbar',
  data() {
    return {
      username: "",
      subject: null,
      message: null,
    };
  },
  created() {
    this.$axios.$get(`/api/auth`).then((response) => {
      this.username = response;
    });
  },
  methods: {
    send() {
      this.$axios
        .$post(`/api/administrators/report`, {
          subject: this.username,
          message: this.message,
        })
        .then((msg) => {
          this.$toast.success(msg).goAway(1500);
        })
        .catch((error) => {
          this.$toast.error("error sending the e-mail").goAway(3000);
        });
    },
  },
};
</script>
