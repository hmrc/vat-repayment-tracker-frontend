(function (d, w) {
  
  function ready(fn) {
    if (d.readyState != 'loading') {
      fn();
    } else {
      d.addEventListener('DOMContentLoaded', fn);
    }
  }

  function toggleClass(el, className) {
    if (el.classList) {
      el.classList.toggle(className);
    } else {
      var classes = el.className.split(' ');
      var existingIndex = classes.indexOf(className);

      if (existingIndex >= 0)
        classes.splice(existingIndex, 1);
      else
        classes.push(className);

      el.className = classes.join(' ');
    }
  }

  function removeClass(el, className) {
    if (el.classList)
      el.classList.remove(className);
    else
      el.className = el.className.replace(new RegExp('(^|\\b)' + className.split(' ').join('|') + '(\\b|$)', 'gi'), ' ');
  }

  function addClass(el, className) {
    if (el.classList)
      el.classList.add(className);
    else
      el.className += ' ' + className;
  }

  ready(function() {
    var mobileMediaQuery = '(max-width: 641px)'
    var mq = w.matchMedia(mobileMediaQuery)
    var inProgess_tab = document.getElementById("tab_inProgress");
    var h3 = inProgess_tab.parentNode
    var result = window.getComputedStyle(h3, ':before');
    if(!mq.matches || !result) {
        jsTabs()
    }
  })

  function jsTabs() {
    var tabs = d.querySelectorAll('.govuk-tabs')
    var tabSelectedClass = 'govuk-tabs__tab--selected'
    var panelHiddenClass = 'govuk-tabs__panel--hidden'
    var listItemSelectedClass = 'govuk-tabs__list-item--selected'


    Array.prototype.forEach.call(tabs, function (tabsEl, i) {

      var tabLinks = tabsEl.querySelectorAll('.govuk-tabs__tab')
      var tabPanels = tabsEl.querySelectorAll('.govuk-tabs__panel')

      Array.prototype.forEach.call(tabLinks, function (tabLink) {

        //setup event listener for tabs
        tabLink.addEventListener('click', function(event) {
              var inProgess_tab = document.getElementById("tab_inProgress");
              var h3 = inProgess_tab.parentNode
              var result = window.getComputedStyle(h3, ':before').content;
              if(result == "none") {
                event.preventDefault()
                tabClick(this)
              }
        })

        tabLink.addEventListener('keydown', function(event) {
            var inProgess_tab = document.getElementById("tab_inProgress");
            var h3 = inProgess_tab.parentNode
            var result = window.getComputedStyle(h3, ':before').content;
            if(result == "none") {
                tabKeyPress(this)
            }
        })

      })


      function tabClick(tab) {
        var inProgess_tab = document.getElementById("tab_inProgress");
        var completed_tab = document.getElementById("tab_completed");
        var sectionInProcess = document.getElementById("inProgress")
        var sectionCompleted = document.getElementById("completed")
        //remove all tabSelectedClass from all tabs
        //change aria-selected to false for all tabs
        Array.prototype.forEach.call(tabLinks, function (tabLink) {
          removeClass(tabLink.parentNode, listItemSelectedClass)
          removeClass(tabLink, tabSelectedClass)
          tabLink.setAttribute('aria-selected', 'false')
          tabLink.setAttribute('tabindex', '-1')
          tabLink.setAttribute('role', 'tab')
          tabLink.parentNode.setAttribute('role', 'presentation')

        })

        //add selected class to the selected tab
        //change aria-selected to true for the selected tab
        addClass(tab, tabSelectedClass)
        tab.setAttribute('aria-selected', 'true')
        tab.setAttribute('tabindex', '0')
        // Adding the aria-controls to the tab links on page load
        inProgess_tab.setAttribute("aria-controls", "inProgress");
        completed_tab.setAttribute("aria-controls", "completed");
        sectionInProcess.setAttribute("aria-labelledby", "tab_inProgress");
        sectionCompleted.setAttribute("aria-labelledby", "tab_completed");
        sectionInProcess.setAttribute("role", "tabpanel");
        sectionCompleted.setAttribute("role", "tabpanel");


        //hide all the panels
        Array.prototype.forEach.call(tabPanels, function (tabPanel) {
          addClass(tabPanel, panelHiddenClass)
        })

        //show the target panel
        var targetPanel = tab.getAttribute('href')
        var panel = tabsEl.querySelector(targetPanel)
        removeClass(panel, panelHiddenClass)
        addClass(tab.parentNode, listItemSelectedClass)
      }

      function tabKeyPress(tab) {
          var tabKeyDownFirst = d.getElementsByClassName('govuk-tabs__tab').item(0);
          var tabKeyDownSecond = d.getElementsByClassName('govuk-tabs__tab').item(1);
          var paneKeyDownFirst  = d.getElementsByClassName('govuk-tabs__panel').item(0);
          var paneKeyDownSecond  = d.getElementsByClassName('govuk-tabs__panel').item(1);
          var inProgess_tab = document.getElementById("tab_inProgress");
          var completed_tab = document.getElementById("tab_completed");
          var sectionInProcess = document.getElementById("inProgress")
          var sectionCompleted = document.getElementById("completed")
          switch (event.keyCode) {
                case 37:
                    Array.prototype.forEach.call(tabLinks, function (tabLink) {
                        removeClass(tabLink, tabSelectedClass)
                        removeClass(tabLink.parentNode, listItemSelectedClass)
                        tabLink.setAttribute('aria-selected', 'false')
                        tabLink.setAttribute('tabindex', '-1')
                        tabLink.setAttribute('role', 'tab')
                        sectionInProcess.setAttribute("aria-labelledby", "tab_inProgress");
                        sectionCompleted.setAttribute("aria-labelledby", "tab_completed");
                    })
                    addClass(tabKeyDownFirst, tabSelectedClass)
                    tabKeyDownFirst.setAttribute('aria-selected', 'true')
                    tabKeyDownFirst.setAttribute('tabindex', '0')
                    tabKeyDownFirst.focus();
                    inProgess_tab.setAttribute("aria-controls", "inProgress");
                    completed_tab.setAttribute("aria-controls", "completed");
                    Array.prototype.forEach.call(tabPanels, function (tabPanel) {
                        addClass(tabPanel, panelHiddenClass)
                        tabPanel.setAttribute("role", "tabpanel");
                    })
                    removeClass(paneKeyDownFirst, panelHiddenClass)
                    addClass(tabKeyDownFirst.parentNode, listItemSelectedClass)
                    break;
                case 40:
                    Array.prototype.forEach.call(tabLinks, function (tabLink) {
                         removeClass(tabLink, tabSelectedClass)
                         removeClass(tabLink.parentNode, listItemSelectedClass)
                         tabLink.setAttribute('aria-selected', 'false')
                         tabLink.setAttribute('tabindex', '-1')
                         tabLink.setAttribute('role', 'tab')
                         sectionInProcess.setAttribute("aria-labelledby", "tab_inProgress");
                         sectionCompleted.setAttribute("aria-labelledby", "tab_completed");
                    })
                    addClass(tabKeyDownSecond, tabSelectedClass)
                    tabKeyDownSecond.setAttribute('aria-selected', 'true')
                    tabKeyDownSecond.setAttribute('tabindex', '0')
                    tabKeyDownSecond.focus();
                    inProgess_tab.setAttribute("aria-controls", "inProgress");
                    completed_tab.setAttribute("aria-controls", "completed");
                    Array.prototype.forEach.call(tabPanels, function (tabPanel) {
                         addClass(tabPanel, panelHiddenClass)
                         tabPanel.setAttribute("role", "tabpanel");
                    })
                    removeClass(paneKeyDownSecond, panelHiddenClass)
                    addClass(tabKeyDownSecond.parentNode, listItemSelectedClass)
                    break;
                case 39:
                    Array.prototype.forEach.call(tabLinks, function (tabLink) {
                         removeClass(tabLink, tabSelectedClass)
                         removeClass(tabLink.parentNode, listItemSelectedClass)
                         tabLink.setAttribute('aria-selected', 'false')
                         tabLink.setAttribute('tabindex', '-1')
                         tabLink.setAttribute('role', 'tab')
                         sectionInProcess.setAttribute("aria-labelledby", "tab_inProgress");
                         sectionCompleted.setAttribute("aria-labelledby", "tab_completed");
                    })
                    addClass(tabKeyDownSecond, tabSelectedClass)
                    tabKeyDownSecond.setAttribute('aria-selected', 'true')
                    tabKeyDownSecond.setAttribute('tabindex', '0')
                    tabKeyDownSecond.focus();
                    inProgess_tab.setAttribute("aria-controls", "inProgress");
                    completed_tab.setAttribute("aria-controls", "completed");
                    Array.prototype.forEach.call(tabPanels, function (tabPanel) {
                         addClass(tabPanel, panelHiddenClass)
                         tabPanel.setAttribute("role", "tabpanel");
                    })
                    removeClass(paneKeyDownSecond, panelHiddenClass)
                    addClass(tabKeyDownSecond.parentNode, listItemSelectedClass)
                    break;
                case 38:
                    Array.prototype.forEach.call(tabLinks, function (tabLink) {
                         removeClass(tabLink, tabSelectedClass)
                         removeClass(tabLink.parentNode, listItemSelectedClass)
                         tabLink.setAttribute('aria-selected', 'false')
                         tabLink.setAttribute('tabindex', '-1')
                         tabLink.setAttribute('role', 'tab')
                         sectionInProcess.setAttribute("aria-labelledby", "tab_inProgress");
                         sectionCompleted.setAttribute("aria-labelledby", "tab_completed");
                    })
                    addClass(tabKeyDownFirst, tabSelectedClass)
                    tabKeyDownFirst.setAttribute('aria-selected', 'true')
                    tabKeyDownFirst.setAttribute('tabindex', '0')
                    inProgess_tab.setAttribute("aria-controls", "inProgress");
                    completed_tab.setAttribute("aria-controls", "completed");
                    tabKeyDownFirst.focus();
                    Array.prototype.forEach.call(tabPanels, function (tabPanel) {
                         addClass(tabPanel, panelHiddenClass)
                         tabPanel.setAttribute("role", "tabpanel");
                    })
                    removeClass(paneKeyDownFirst, panelHiddenClass)
                    addClass(tabKeyDownFirst.parentNode, listItemSelectedClass)
                              break;
          }
      }

    })
}

})(document,window);

window.addEventListener("load", function(event) {
    var errorSummary = document.getElementById("error-summary-display");
    var errorSummarylinkHref = document.querySelector("[href='#manage']");
    var skiplinkTag = document.querySelector("[href='#content']");
    if(errorSummary) errorSummary.focus();
    // This changes the error summary link's href from #maage to #dd
    // This was done to focus the error summary link on the first radio button on the manage_or_track.scala.html page
    if(errorSummarylinkHref) errorSummarylinkHref.setAttribute('href', '#dd');
    // Changing the skiplink href to bypass the breadcrumbs main-message
    if(skiplinkTag) skiplinkTag.setAttribute('href', '#main-message');
});


function myFunction(x) {
  var inProgess_tab = document.getElementById("tab_inProgress");
  var completed_tab = document.getElementById("tab_completed");
  if (x.matches) { // If media query matches
    if(completed_tab) {
        completed_tab.removeAttribute("aria-controls");
        completed_tab.removeAttribute("aria-selected");
        completed_tab.removeAttribute("tabindex");
        completed_tab.removeAttribute("role");
    }
    if(inProgess_tab) {
        inProgess_tab.removeAttribute("aria-controls");
        inProgess_tab.removeAttribute("aria-selected");
        inProgess_tab.removeAttribute("tabindex");
        inProgess_tab.removeAttribute("role");
    }
  }
}


var x = window.matchMedia("(max-width: 641px)")
myFunction(x) // Call listener function at run time
x.addListener(myFunction) // Attach listener function on state changes


window.addEventListener('resize', function(){
    var inProgess_tab = document.getElementById("tab_inProgress");
    var completed_tab = document.getElementById("tab_completed");
    var sectionInProcess = document.getElementById("inProgress")
    var sectionCompleted = document.getElementById("completed")
    var tabSelectedClass = document.getElementsByClassName("govuk-tabs__list-item--selected");
    var tabHiddenClass = document.getElementsByClassName("govuk-tabs__list-item--selected");
    var h3 = inProgess_tab.parentNode
    var result = window.getComputedStyle(h3, ':before');
    if(result) {
           if(tabSelectedClass) {
               inProgess_tab.parentNode.classList.remove("govuk-tabs__tab--selected");
           }
           if(tabHiddenClass) {
               inProgess_tab.parentNode.classList.remove("govuk-tabs__panel--hidden")
           }
               inProgess_tab.parentNode.removeAttribute("role");
               sectionInProcess.removeAttribute("role");
               sectionInProcess.removeAttribute("aria-labelledby");

               completed_tab.parentNode.removeAttribute("role");
               sectionCompleted.removeAttribute("role");
               sectionCompleted.removeAttribute("aria-labelledby");
           if(tabSelectedClass) {
               completed_tab.parentNode.classList.remove("govuk-tabs__tab--selected");
           }
           if(tabHiddenClass) {
               completed_tab.parentNode.classList.remove("govuk-tabs__panel--hidden")
           }
        }
})



