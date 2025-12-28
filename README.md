# SeeTnT — Кастомные TNT для Minecraft 1.16.5+

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Minecraft](https://img.shields.io/badge/Minecraft-1.16.5%2B-green)](https://www.minecraft.net/)
[![Java](https://img.shields.io/badge/Java-17%2B-orange)](https://www.java.com/)

SeeTnT — это мощный плагин для Spigot/Paper серверов Minecraft, который позволяет создавать полностью настраиваемые TNT с уникальными характеристиками, визуальными эффектами и механиками взрыва.

---

## ✨ **Возможности**

### 🎯 **Основные функции**
- **Кастомные TNT** с уникальными ID и настройками
- **Два типа взрыва**: Куб (CUBE) или Сфера (SPHERE)
- **Настраиваемый радиус** и **время до взрыва**
- **Система фильтров блоков**: белый/чёрный список разрушаемых блоков
- **Вероятность разрушения** (параметр `power`)

### 🎨 **Визуальные эффекты**
- **Голограмма** над TNT с отсчётом времени
- **BossBar** с прогресс-баром и настраиваемым цветом
- **ActionBar** уведомления
- **Title** сообщения
- **Свечение** предмета TNT (glow эффект)
- **Поддержка HEX-цветов** (`&#RRGGBB`)

### ⚡ **Особые механики**
- **Автоподжиг** TNT после установки
- **Взрыв в воде/лаве** (настраивается)
- **PersistentDataContainer** для хранения ID TNT
- **Команда для выдачи** TNT игрокам
- **Полная перезагрузка** конфигурации без перезапуска сервера

---

## 📦 **Установка**

1. **Требования:**
   - Minecraft Server 1.16.5 или выше
   - Java 17 или выше
   - Spigot/Paper сервер
   - [ProtocolLib](https://www.spigotmc.org/resources/protocollib.1997/) (обязательно)

2. **Установка:**
   - Скачайте `SeeTnT.jar` из [Releases](https://github.com/yourusername/SeeTnT/releases)
   - Поместите файл в папку `plugins/`
   - Перезапустите сервер

---

## ⚙️ **Конфигурация**

Конфигурационный файл `config.yml` автоматически создаётся при первом запуске.

### 📝 **Пример настройки TNT:**
```yaml
tnts:
  obsidian_tnt:
    display_name: "&#F3A26BОбсидиановый тнт"
    lore:
      - "&f "
      - "&b● Взрывает обсидиан"
      - "&b● Повышенный радиус"
      - "&f "
    fuse: 6
    radius: 10
    type: SPHERE
    power: 1.0
    auto_ignite: true
    explode_in_water: false
    explode_in_lava: false
    
    hologram:
      enabled: true
      text: "&b{time} секунд до взрыва!"
    
    bossbar:
      enabled: true
      text: "&bДо взрыва осталось: {time} секунд!"
      progress: true
      color: RED
    
    glow: true
    
    whitelist_blocks:
      enabled: true
      blocks:
        - "OBSIDIAN"
        - "CRYING_OBSIDIAN"
```

### 🔧 **Доступные параметры:**
| Параметр | Описание | По умолчанию |
|----------|----------|--------------|
| `fuse` | Время до взрыва (секунды) | 5 |
| `radius` | Радиус взрыва | 3 |
| `type` | Тип взрыва: `SPHERE` или `CUBE` | `SPHERE` |
| `power` | Вероятность разрушения (0.0-1.0) | 1.0 |
| `auto_ignite` | Автоподжиг при установке | `true` |
| `explode_in_water` | Взрыв в воде | `true` |
| `explode_in_lava` | Взрыв в лаве | `true` |
| `glow` | Свечение предмета | `false` |

---

## 🎮 **Использование**

### 🔧 **Команды:**
```
/seetnt reload                    - Перезагрузить конфигурацию
/seetnt give <игрок> <id> <кол-во> - Выдать кастомную TNT
```

**Пример:**
```
/seetnt give Notch obsidian_tnt 5
```

### 🏗 **Создание TNT в игре:**
1. Настройте TNT в `config.yml`
2. Перезагрузите плагин: `/seetnt reload`
3. Выдайте TNT игроку
4. Установите TNT как обычный блок

---

## 🛠 **Разработчикам**

### 📁 **Структура проекта:**
```
SeeTnT/
├── src/main/java/ru/gdev/seetnt/
│   ├── SeeTnT.java           # Главный класс
│   ├── TNTManager.java       # Менеджер TNT
│   ├── CustomTNT.java        # Модель данных TNT
│   ├── TNTListener.java      # Обработчик событий
│   ├── ItemMetaUtil.java     # Работа с предметами
│   ├── Utils.java           # Утилиты
│   └── RestTNTCommand.java   # Команды
├── src/main/resources/
│   ├── plugin.yml           # Описание плагина
│   └── config.yml           # Конфигурация
└── pom.xml                  # Maven конфигурация
```

### 🔌 **API использование:**
```java
// Получить экземпляр плагина
SeeTnT plugin = SeeTnT.getInstance();

// Получить менеджер TNT
TNTManager manager = plugin.getTNTManager();

// Получить кастомную TNT по ID
CustomTNT tnt = manager.getTNT("obsidian_tnt");

// Создать предмет TNT
ItemStack item = ItemMetaUtil.createTNTItem(tnt, 5, plugin);
```

---

## 📋 **Зависимости**

- **Spigot API 1.16.5+**
- **ProtocolLib 5.1.0+**
- **Java 17+**

---

## 🐛 **Решение проблем**

| Проблема | Решение |
|----------|---------|
| TNT не взрывается | Проверьте настройки `explode_in_water` и `explode_in_lava` |
| Не работает голограмма | Убедитесь, что `hologram.enabled: true` |
| Ошибка с ProtocolLib | Обновите ProtocolLib до последней версии |
| TNT не выдаётся | Проверьте ID TNT в команде и конфиге |

---

## 🤝 **Вклад в проект**

Мы приветствуем ваш вклад! Пожалуйста:

1. Форкните репозиторий
2. Создайте ветку для вашей функции (`git checkout -b feature/amazing-feature`)
3. Зафиксируйте изменения (`git commit -m 'Add amazing feature'`)
4. Запушьте ветку (`git push origin feature/amazing-feature`)
5. Откройте Pull Request

---

## 📄 **Лицензия**

Этот проект лицензирован под **MIT License** - смотрите файл [LICENSE](LICENSE) для подробностей.

---

## 👨‍💻 **Автор**

- [GDev](https://gdev.seemine.su)
- **Поддержка**: Создайте Issue на GitHub

---

## ⭐ **Поддержка проекта**

Если вам нравится этот плагин, поставьте звезду на GitHub! ⭐

---

**Версия:** 1.0-latest  
**Дата релиза:** 2025  
**Совместимость:** Minecraft 1.16.4 - 1.17.x
