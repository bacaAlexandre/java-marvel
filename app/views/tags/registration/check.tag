#{if session.username && controllers.Registration.check(_arg)}
    #{doBody /}
#{/if}