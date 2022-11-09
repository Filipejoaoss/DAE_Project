
export default function({$auth,redirect,route}){
  if(!$auth.user.groups.includes("Patient") || !$auth.user.groups.includes("Medic") || !$auth.user.groups.includes("Administrator") ) {
    return redirect('/login')
  }
}
