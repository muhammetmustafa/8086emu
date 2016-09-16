# 8086emu

Eski Intel 8086 işlemcisinin çalışma mantığını daha iyi anlatabilmek için okul projesi kapsamında gerçeklediğim bir projedir. Bazı kısımları tam çalışmamaktadır.

Proje;
      - Basit bir assembly derleyicisinden (sadece 8086'ının çalıştırabildiği komutlar),
      - Memory biriminden,
      - Adres, Veri ve Kontrol yolundan,
      - IQ (Instruction Queue - Komut Kuyruğu)'dan,
      - ALU (Arithmetic Logic Unit)'dan,
      - BIU (Bus Interface Unit)'dan (bu birim ayrı thread olarak çalışmaktadır)
      - EU (Execution Unit)'dan (bu birim ayrı thread olarak çalışmaktadır)

Simülasyonun temel işleyişi şöyledir:
- Assembly kodunun derlenmesi ve başarılıysa programın bellek birimine yüklenmesi
- BIU ve EU birimlerinin ayrı thread'lerde çalıştırılması.
  Bu aşamada BIU'nun görevi bellekten komut okuyup komut kuyruğuna ekleme veya EU'nun isteği üzerine bellekten okuma/belleğe yazma işlemini gerçekleştirmektir.
  EU'nun görevi ise komut kuyruğundan sıradaki komutu alıp bunu çalıştırmak (ALU'ya göndermek veya bellek işlemi ise BIU'ya aktarmak).
