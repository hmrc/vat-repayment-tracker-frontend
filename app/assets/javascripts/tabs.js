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
    if(!mq.matches) { jsTabs() }
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
          event.preventDefault()
          tabClick(this)
        })

        tabLink.addEventListener('keydown', function(event) {
          var tabKeyDownInProgress = d.getElementById('tab_inProgress')
          var tabKeyDownCompleted  = d.getElementById('tab_completed')
          switch (event.keyCode) {
              case 37:
                   Array.prototype.forEach.call(tabLinks, function (tabLink) {
                      removeClass(tabLink, tabSelectedClass)
                      removeClass(tabLink.closest('li'), listItemSelectedClass)
                      tabLink.setAttribute('aria-selected', 'false')
                      tabLink.setAttribute('tabindex', '-1')
                   })

                   addClass(tabKeyDownInProgress, tabSelectedClass)
                   tabKeyDownInProgress.setAttribute('aria-selected', 'true')
                   tabKeyDownInProgress.setAttribute('tabindex', '0')
                   tabKeyDownInProgress.focus();
                   Array.prototype.forEach.call(tabPanels, function (tabPanel) {
                    addClass(tabPanel, panelHiddenClass)
                   })

                   removeClass(d.getElementById('inProgress'), panelHiddenClass)
                   addClass(d.getElementById('tab_inProgress').closest('li'), listItemSelectedClass)
                  break;
              case 39:
                  Array.prototype.forEach.call(tabLinks, function (tabLink) {
                     removeClass(tabLink, tabSelectedClass)
                     removeClass(tabLink.closest('li'), listItemSelectedClass)
                     tabLink.setAttribute('aria-selected', 'false')
                     tabLink.setAttribute('tabindex', '-1')
                  })

                  addClass(tabKeyDownCompleted, tabSelectedClass)
                  tabKeyDownCompleted.setAttribute('aria-selected', 'true')
                  tabKeyDownCompleted.setAttribute('tabindex', '0')
                  tabKeyDownCompleted.focus();
                  Array.prototype.forEach.call(tabPanels, function (tabPanel) {
                   addClass(tabPanel, panelHiddenClass)
                  })

                  removeClass(d.getElementById('completed'), panelHiddenClass)
                  addClass(d.getElementById('tab_completed').closest('li'), listItemSelectedClass)
                  break;
              }

        })

      })


      function tabClick(tab) {

        //remove all tabSelectedClass from all tabs
        //change aria-selected to false for all tabs
        Array.prototype.forEach.call(tabLinks, function (tabLink) {
          removeClass(tabLink, tabSelectedClass)
          removeClass(tabLink.closest('li'), listItemSelectedClass)
          tabLink.setAttribute('aria-selected', 'false')
          tabLink.setAttribute('tabindex', '-1')
        })

        //add selected class to the selected tab
        //change aria-selected to true for the selected tab
        addClass(tab, tabSelectedClass)
        tab.setAttribute('aria-selected', 'true')
        tab.setAttribute('tabindex', '0')

        //hide all the panels
        Array.prototype.forEach.call(tabPanels, function (tabPanel) {
          addClass(tabPanel, panelHiddenClass)
        })

        //show the target panel
        var targetPanel = tab.getAttribute('href')
        var panel = tabsEl.querySelector(targetPanel)
        removeClass(panel, panelHiddenClass)
        addClass(tab.closest('li'), listItemSelectedClass)
      }
    })
  }

})(document,window);

window.addEventListener("load", function(event) {
    var errorSummary = document.getElementById("error-summary-display");
    var errorSummarylinkHref = document.querySelector("[href='#manage']");
    if(errorSummary) errorSummary.focus();
    // This changes the error summary link's href from #maage to #dd
    // This was done to focus the error summary link on the first radio button on the manage_or_track.scala.html page
    if(errorSummarylinkHref) errorSummarylinkHref.setAttribute('href', '#dd');
});


