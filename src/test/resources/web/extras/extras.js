// Extras practice page. Plain JavaScript, no libraries.
(function () {
  'use strict';

  // 1. Transfer: a confirm() dialog, then an alert() with the result.
  document.getElementById('transferButton').addEventListener('click', function () {
    var result = document.getElementById('transferResult');
    if (confirm('Confirm this transfer?')) {
      result.textContent = 'Transfer complete: $100.00 to account 54321.';
      alert('Transfer complete: $100.00 to account 54321.');
    } else {
      result.textContent = 'Transfer cancelled.';
      alert('Transfer cancelled.');
    }
  });

  // 2. The help chat is in chat.html, inside the iframe.

  // 3. Statement upload: shows the file name, size and number of CSV data rows
  //    (non-empty lines after the header). Everything happens in the browser, there is no server.
  document.getElementById('statementFile').addEventListener('change', function (e) {
    var f = e.target.files[0];
    var out = document.getElementById('uploadResult');
    if (!f) { out.textContent = 'No file.'; return; }
    var reader = new FileReader();
    reader.onload = function () {
      var lines = String(reader.result).split(/\r?\n/).filter(function (l) { return l.trim() !== ''; });
      var rows = Math.max(0, lines.length - 1);
      out.textContent = 'Received ' + f.name + ' (' + f.size + ' bytes, ' + rows + ' rows).';
    };
    reader.readAsText(f);
  });

  // 4. Account menu: opened by CSS :hover. Clicking an item shows which one was chosen.
  document.querySelectorAll('#accountMenuItems a').forEach(function (a) {
    a.addEventListener('click', function (e) {
      e.preventDefault();
      document.getElementById('menuResult').textContent = 'You chose: ' + a.textContent + '.';
    });
  });

  // 5. Favourite payees: drag with pointer events (what WebDriver Actions sends), not HTML5 drag and drop.
  var list = document.getElementById('payees');
  var dragged = null, startY = 0;

  function order() {
    return Array.prototype.map.call(list.children, function (li) { return li.dataset.payee; });
  }
  function showOrder() {
    document.getElementById('payeeOrder').textContent = 'Order: ' + order().join(', ');
  }

  list.addEventListener('pointerdown', function (e) {
    var li = e.target.closest('li');
    if (!li) return;
    dragged = li;
    startY = e.clientY;
    li.classList.add('dragging');
    e.preventDefault();
  });
  document.addEventListener('pointermove', function (e) {
    if (!dragged) return;
    dragged.style.top = (e.clientY - startY) + 'px';
  });
  document.addEventListener('pointerup', function (e) {
    if (!dragged) return;
    var li = dragged;
    dragged = null;
    li.classList.remove('dragging');
    li.style.top = '';
    // Insert the item before the first item whose middle is below the pointer.
    var others = Array.prototype.filter.call(list.children, function (c) { return c !== li; });
    var before = null;
    for (var i = 0; i < others.length; i++) {
      var r = others[i].getBoundingClientRect();
      if (e.clientY < r.top + r.height / 2) { before = others[i]; break; }
    }
    list.insertBefore(li, before);
    showOrder();
  });
  showOrder();
})();
