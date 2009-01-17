application {
    title='Technorati'
    startupGroups = ['Technorati']

    // If you want some non-standard application class, apply it here
    //frameClass = 'javax.swing.JFrame'
}
mvcGroups {
    // MVC Group for "Technorati"
    Technorati {
        model = 'TechnoratiModel'
        view = 'TechnoratiView'
        controller = 'TechnoratiController'
    }

}
