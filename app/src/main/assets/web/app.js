// Arso Ader Web Application Engine
document.addEventListener('DOMContentLoaded', () => {
  // Navigation Tabs
  const navButtons = document.querySelectorAll('.nav-btn');
  const tabPanes = document.querySelectorAll('.tab-pane');

  navButtons.forEach(btn => {
    btn.addEventListener('click', () => {
      const targetTab = btn.getAttribute('data-tab');
      navButtons.forEach(b => b.classList.remove('active'));
      tabPanes.forEach(p => p.classList.remove('active'));

      btn.classList.add('active');
      const targetPane = document.getElementById(targetTab);
      if (targetPane) targetPane.classList.add('active');

      window.scrollTo({ top: 0, behavior: 'smooth' });
    });
  });

  // Offline status listener
  const statusBadge = document.getElementById('network-status');
  function updateOnlineStatus() {
    if (statusBadge) {
      if (navigator.onLine) {
        statusBadge.innerHTML = '<span class="status-dot"></span> ኦንላይን (ዝግጁ)';
        statusBadge.style.color = '#FFF';
      } else {
        statusBadge.innerHTML = '<span class="status-dot" style="background:#FFA000"></span> ከመስመር ውጭ (ኦፍላይን)';
        statusBadge.style.color = '#FFF';
      }
    }
  }
  window.addEventListener('online', updateOnlineStatus);
  window.addEventListener('offline', updateOnlineStatus);
  updateOnlineStatus();

  // Register Service Worker for Offline PWA
  if ('serviceWorker' in navigator) {
    navigator.serviceWorker.register('./sw.js').catch(err => {
      console.log('SW registration note:', err);
    });
  }

  // Land Calculator Logic
  const hectaresInput = document.getElementById('calc-hectares');
  const timadOutput = document.getElementById('calc-timad');
  const npsbOutput = document.getElementById('calc-npsb');
  const ureaOutput = document.getElementById('calc-urea');
  const taxOutput = document.getElementById('calc-tax');

  function calculateLand() {
    if (!hectaresInput) return;
    const ha = parseFloat(hectaresInput.value) || 0;
    const timad = ha * 4;
    const npsb = Math.ceil(ha * 1.5);
    const urea = Math.ceil(ha * 1.5);
    const tax = ha * 190;

    if (timadOutput) timadOutput.textContent = timad.toFixed(1) + ' ጥማድ (ቃዳ)';
    if (npsbOutput) npsbOutput.textContent = npsb + ' ኩንታል';
    if (ureaOutput) ureaOutput.textContent = urea + ' ኩንታል';
    if (taxOutput) taxOutput.textContent = tax.toLocaleString() + ' ብር';
  }

  if (hectaresInput) {
    hectaresInput.addEventListener('input', calculateLand);
    calculateLand();
  }

  // Marketplace demo data & local storage
  const sampleCrops = [
    { name: 'ማኛ ነጭ ጤፍ (Magna Teff)', price: 9200, qty: 35, location: 'መርዓዊ ቀበሌ 01', phone: '0918234567' },
    { name: 'የኩክሳ ነጭ ስንዴ (Wheat)', price: 4600, qty: 60, location: 'ይስማላ', phone: '0918765432' },
    { name: 'ድቅል በቆሎ (Maize BH-661)', price: 3400, qty: 85, location: 'አዴት ቀበሌ', phone: '0922334455' }
  ];

  const cropsContainer = document.getElementById('crops-list');
  function renderCrops() {
    if (!cropsContainer) return;
    const crops = JSON.parse(localStorage.getItem('arso_crops')) || sampleCrops;
    cropsContainer.innerHTML = '';
    crops.forEach((c, idx) => {
      const item = document.createElement('div');
      item.className = 'item-card';
      item.innerHTML = `
        <div class="item-main">
          <h4>🌾 ${c.name}</h4>
          <div class="item-meta">📍 ${c.location} • ቀሪ ክምችት፦ ${c.qty} ኩንታል</div>
          <div class="item-meta">📞 ስልክ፦ <a href="tel:${c.phone}" style="color:var(--primary);font-weight:700">${c.phone}</a></div>
        </div>
        <div style="text-align:right">
          <div class="item-price">${c.price.toLocaleString()} ብር</div>
          <button class="btn btn-secondary" style="padding:6px 12px;font-size:0.8rem;margin-top:6px;width:auto" onclick="recordLikeAndAlert('${c.name}')">❤️ ላይክ & ሼር</button>
        </div>
      `;
      cropsContainer.appendChild(item);
    });
  }
  renderCrops();

  window.recordLikeAndAlert = function(cropName) {
    recordEngagement('like');
    alert(`የምርት መረጃውን ስለወደዱ እና ስላጋሩ እናመሰግናለን! (${cropName})`);
  };

  // Add Crop Form
  const addCropForm = document.getElementById('form-add-crop');
  if (addCropForm) {
    addCropForm.addEventListener('submit', (e) => {
      e.preventDefault();
      const name = document.getElementById('crop-name').value;
      const price = parseFloat(document.getElementById('crop-price').value) || 0;
      const qty = parseFloat(document.getElementById('crop-qty').value) || 0;
      const phone = document.getElementById('crop-phone').value;
      const loc = document.getElementById('crop-loc').value;

      const crops = JSON.parse(localStorage.getItem('arso_crops')) || sampleCrops;
      crops.unshift({ name, price, qty, location: loc, phone });
      localStorage.setItem('arso_crops', JSON.stringify(crops));
      recordEngagement('registration');

      alert('ምርትዎ ያለ ደላላ በቀጥታ ወደ ገበያው ገብቷል!');
      addCropForm.reset();
      renderCrops();
    });
  }

  // Engagement & Monetization Engine
  function getMonetizationData() {
    let data = JSON.parse(localStorage.getItem('arso_monetization'));
    if (!data) {
      data = {
        registrations: 184,
        shares: 96,
        likes: 1420,
        vasEarned: 4250,
        regRate: 25,
        shareRate: 5,
        likeRate: 1,
        balance: 9850,
        withdrawn: 6500,
        method: 'Telebirr',
        account: '0921458976',
        name: 'Zebene Asfye',
        history: [
          { id: 'TX-99824102', amount: 4000, method: 'Telebirr', account: '0921458976', date: 'ትናንት' },
          { id: 'TX-88291044', amount: 2500, method: 'CBE Birr', account: '1000284910294', date: 'ባለፈው ሳምንት' }
        ]
      };
      localStorage.setItem('arso_monetization', JSON.stringify(data));
    }
    return data;
  }

  function recordEngagement(type) {
    const data = getMonetizationData();
    if (type === 'registration') {
      data.registrations += 1;
      data.balance += data.regRate;
    } else if (type === 'share') {
      data.shares += 1;
      data.balance += data.shareRate;
    } else if (type === 'like') {
      data.likes += 1;
      data.balance += data.likeRate;
    }
    localStorage.setItem('arso_monetization', JSON.stringify(data));
  }

  // Share button
  window.shareWebApp = function() {
    recordEngagement('share');
    if (navigator.share) {
      navigator.share({
        title: 'አርሶ አደር - የኢትዮጵያ ግብርና ዲጂታል ፖርታል',
        text: 'የኢትዮጵያ አርሶ አደሮች የማዳበሪያ መውሰጃ ተራ፣ የግብአት ክፍያና የሰብል ገበያ ዲጂታል ድረ-ገጽን በስልክዎ ይክፈቱ፦',
        url: window.location.href
      }).catch(() => {});
    } else {
      navigator.clipboard.writeText(window.location.href);
      alert('የድረ-ገጹ ማስፈንጠሪያ (ሊንክ) ኮፒ ተደርጓል! ለሌሎች አርሶ አደሮች ማጋራት ይችላሉ።');
    }
  };

  // Secret Developer Portal Trigger (5 Clicks on footer version badge)
  let secretClickCount = 0;
  const secretTrigger = document.getElementById('secret-trigger');
  const secretModal = document.getElementById('secret-modal');
  const secretPinInput = document.getElementById('secret-pin-input');
  const secretSubmitBtn = document.getElementById('btn-submit-pin');
  const secretDashboard = document.getElementById('secret-dashboard');
  const secretLoginForm = document.getElementById('secret-login-form');

  if (secretTrigger) {
    secretTrigger.addEventListener('click', () => {
      secretClickCount++;
      if (secretClickCount >= 5) {
        secretClickCount = 0;
        if (secretModal) {
          secretModal.classList.add('active');
          if (secretLoginForm) secretLoginForm.style.display = 'block';
          if (secretDashboard) secretDashboard.style.display = 'none';
          if (secretPinInput) secretPinInput.value = '';
        }
      }
    });
  }

  window.closeSecretModal = function() {
    if (secretModal) secretModal.classList.remove('active');
  };

  if (secretSubmitBtn) {
    secretSubmitBtn.addEventListener('click', () => {
      const pin = secretPinInput ? secretPinInput.value.trim() : '';
      if (pin === '7788' || pin === '2026') {
        if (secretLoginForm) secretLoginForm.style.display = 'none';
        if (secretDashboard) secretDashboard.style.display = 'block';
        renderSecretDashboard();
      } else {
        alert('የተሳሳተ ሚስጥር ፒን ነው!');
      }
    });
  }

  function renderSecretDashboard() {
    const data = getMonetizationData();
    const balEl = document.getElementById('creator-balance');
    const withEl = document.getElementById('creator-withdrawn');
    const regEl = document.getElementById('creator-reg-count');
    const shareEl = document.getElementById('creator-share-count');
    const likeEl = document.getElementById('creator-like-count');
    const vasEl = document.getElementById('creator-vas-earned');
    const methodSelect = document.getElementById('creator-payout-method');
    const acctInput = document.getElementById('creator-payout-acct');
    const nameInput = document.getElementById('creator-payout-name');
    const historyList = document.getElementById('creator-payout-history');

    if (balEl) balEl.textContent = data.balance.toLocaleString() + ' ETB';
    if (withEl) withEl.textContent = data.withdrawn.toLocaleString() + ' ETB';
    if (regEl) regEl.textContent = data.registrations + ' ምዝገባዎች (' + (data.registrations * data.regRate).toLocaleString() + ' ብር)';
    if (shareEl) shareEl.textContent = data.shares + ' ሼሮች (' + (data.shares * data.shareRate).toLocaleString() + ' ብር)';
    if (likeEl) likeEl.textContent = data.likes + ' ላይኮች (' + (data.likes * data.likeRate).toLocaleString() + ' ብር)';
    if (vasEl) vasEl.textContent = data.vasEarned.toLocaleString() + ' ETB';

    if (methodSelect) methodSelect.value = data.method;
    if (acctInput) acctInput.value = data.account;
    if (nameInput) nameInput.value = data.name;

    if (historyList) {
      historyList.innerHTML = '';
      data.history.forEach(h => {
        const row = document.createElement('div');
        row.style.cssText = 'padding:8px;border-bottom:1px solid #E2E8F0;display:flex;justify-content:space-between;font-size:0.85rem';
        row.innerHTML = `
          <div>
            <b>${h.method}</b> (${h.account})<br>
            <span style="color:#64748B;font-size:0.75rem">${h.id} • ${h.date}</span>
          </div>
          <div style="text-align:right">
            <span style="color:#2E7D32;font-weight:700">+${h.amount.toLocaleString()} ETB</span><br>
            <span style="background:#E8F5E9;color:#1B5E20;padding:2px 6px;border-radius:4px;font-size:0.7rem">የተከፈለ</span>
          </div>
        `;
        historyList.appendChild(row);
      });
    }
  }

  // Withdraw payout in secret dashboard
  window.requestCreatorPayout = function() {
    const data = getMonetizationData();
    const withdrawAmtInput = document.getElementById('creator-withdraw-amt');
    const amt = parseFloat(withdrawAmtInput ? withdrawAmtInput.value : 0) || 0;
    if (amt <= 0) {
      alert('እባክዎ ትክክለኛ የብር መጠን ያስገቡ!');
      return;
    }
    if (amt > data.balance) {
      alert('በቂ ያልሆነ ቀሪ ሂሳብ!');
      return;
    }

    data.balance -= amt;
    data.withdrawn += amt;
    const txId = 'TX-' + Math.floor(10000000 + Math.random() * 90000000);
    data.history.unshift({
      id: txId,
      amount: amt,
      method: data.method,
      account: data.account,
      date: 'አሁን የተፈጸመ'
    });
    localStorage.setItem('arso_monetization', JSON.stringify(data));
    alert(`ክፍያ ${amt.toLocaleString()} ብር ወደ ${data.method} (${data.account}) በተሳካ ሁኔታ ተላልፏል! የማመሳከሪያ ቁጥር፦ ${txId}`);
    renderSecretDashboard();
  };

  window.saveCreatorPayoutDetails = function() {
    const data = getMonetizationData();
    const methodSelect = document.getElementById('creator-payout-method');
    const acctInput = document.getElementById('creator-payout-acct');
    const nameInput = document.getElementById('creator-payout-name');

    if (methodSelect) data.method = methodSelect.value;
    if (acctInput) data.account = acctInput.value;
    if (nameInput) data.name = nameInput.value;

    localStorage.setItem('arso_monetization', JSON.stringify(data));
    alert('የክፍያ መቀበያ አካውንት መረጃዎ በተሳካ ሁኔታ ተመዝግቧል!');
  };
});
