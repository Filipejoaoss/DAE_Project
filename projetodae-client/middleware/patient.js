export default function({$auth,redirect,route}){
  if($auth.user.groups.includes("Patient") && $auth.user.sub != route.params.username) {
    return redirect('/patients/' + $auth.user.sub)
  }
}