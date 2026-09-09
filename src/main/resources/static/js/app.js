// Adapté depuis resources/js/app.js (Laravel/Vite) pour un usage direct navigateur.
// Nécessite que flatpickr (+ locale fr) soit chargé au préalable via CDN dans le layout.
(function () {
    if (window.flatpickr && window.flatpickr.l10ns && window.flatpickr.l10ns.fr) {
        flatpickr.localize(flatpickr.l10ns.fr);
    }

    function formatAndValidateDateInput(inputVal) {
        let digits = inputVal.replace(/\D/g, '').slice(0, 8);
        let day = '', month = '', year = '';

        if (digits.length >= 1) {
            day = digits.slice(0, 2);
            if (day.length === 1 && parseInt(day, 10) > 3) {
                day = '0' + day;
            } else if (day.length === 2) {
                let dNum = parseInt(day, 10);
                if (dNum === 0) day = '01';
                if (dNum > 31) day = '31';
            }
        }

        if (digits.length >= 3) {
            month = digits.slice(2, 4);
            if (month.length === 1 && parseInt(month, 10) > 1) {
                month = '0' + month;
            } else if (month.length === 2) {
                let mNum = parseInt(month, 10);
                if (mNum === 0) month = '01';
                if (mNum > 12) month = '12';
            }
        }

        if (digits.length >= 5) {
            year = digits.slice(4, 8);
        }

        if (day.length === 2 && month.length === 2) {
            let mNum = parseInt(month, 10);
            let yNum = year.length === 4 ? parseInt(year, 10) : new Date().getFullYear();
            let maxDays = new Date(yNum, mNum, 0).getDate();
            if (parseInt(day, 10) > maxDays) {
                day = String(maxDays).padStart(2, '0');
            }
        }

        let formatted = day;
        if (month || digits.length >= 3) formatted += '/' + month;
        if (year || digits.length >= 5) formatted += '/' + year;

        return {
            formatted: formatted,
            dayNum: day ? parseInt(day, 10) : null,
            monthNum: month ? parseInt(month, 10) : null,
            yearNum: year.length === 4 ? parseInt(year, 10) : null,
            digitsLength: digits.length,
            rawYear: year
        };
    }

    function initDatePickers() {
        if (!window.flatpickr) return;

        const pickers = document.querySelectorAll('.date-picker');
        pickers.forEach(input => {
            input.removeAttribute('readonly');

            input.addEventListener('keydown', function (e) {
                const allowedKeys = ['Backspace', 'Tab', 'Delete', 'ArrowLeft', 'ArrowRight', 'Home', 'End', 'Enter'];
                if (allowedKeys.includes(e.key) || e.ctrlKey || e.metaKey) {
                    return;
                }
                if (!/^\d$/.test(e.key)) {
                    e.preventDefault();
                }
            });

            let fp = input._flatpickr;
            if (!fp) {
                fp = flatpickr(input, {
                    dateFormat: 'd/m/Y',
                    allowInput: true,
                    disableMobile: true,
                    clickOpens: true,
                    parseDate: (dateStr) => {
                        if (/^\d{2}\/\d{2}\/\d{4}$/.test(dateStr)) {
                            const parts = dateStr.split('/');
                            const d = parseInt(parts[0], 10);
                            const m = parseInt(parts[1], 10) - 1;
                            const y = parseInt(parts[2], 10);
                            if (y >= 1900 && y <= 2100 && m >= 0 && m <= 11 && d >= 1 && d <= 31) {
                                return new Date(y, m, d);
                            }
                        }
                        return undefined;
                    }
                });
            }

            let isProcessingInput = false;

            input.addEventListener('input', function () {
                if (isProcessingInput) return;
                isProcessingInput = true;

                try {
                    let res = formatAndValidateDateInput(input.value);
                    input.value = res.formatted;

                    if (res.monthNum && res.monthNum >= 1 && res.monthNum <= 12) {
                        if (!res.rawYear || res.yearNum) {
                            let currentYear = res.yearNum || new Date().getFullYear();
                            let jumpDate = new Date(currentYear, res.monthNum - 1, 1);
                            fp.jumpToDate(jumpDate, false);
                        }
                    }

                    if (res.digitsLength === 8 && res.yearNum && res.monthNum && res.dayNum) {
                        let fullDateStr = `${String(res.dayNum).padStart(2, '0')}/${String(res.monthNum).padStart(2, '0')}/${res.yearNum}`;
                        fp.setDate(fullDateStr, false, 'd/m/Y');
                    }
                } finally {
                    isProcessingInput = false;
                }
            });

            input.addEventListener('blur', function () {
                let val = input.value.trim();
                if (!val) return;

                let parts = val.split('/');
                if (parts.length === 3) {
                    let d = parts[0];
                    let m = parts[1];
                    let y = parts[2];

                    if (y.length === 2) {
                        y = '20' + y;
                        input.value = `${d}/${m}/${y}`;
                        fp.setDate(`${d}/${m}/${y}`, false, 'd/m/Y');
                    } else if (y.length > 0 && y.length < 4) {
                        let currentYear = String(new Date().getFullYear());
                        y = currentYear.slice(0, 4 - y.length) + y;
                        input.value = `${d}/${m}/${y}`;
                        fp.setDate(`${d}/${m}/${y}`, false, 'd/m/Y');
                    }
                }
            });
        });
    }

    document.addEventListener('DOMContentLoaded', initDatePickers);
    window.initDatePickers = initDatePickers;
})();
