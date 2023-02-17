package question

import (
	"context"
	"github.com/go-redis/redis/v8"
	"gorm.io/gorm"
	"gurobot.cn/scanpen_question_bank/app/component"
	"gurobot.cn/scanpen_question_bank/app/domain"
)

type trieRepository struct {
	Tire    *Trie
	GormDB  *gorm.DB
	Redis   *redis.Client
	version string
	Timeout string
	Ctx     context.Context
}

func NewTrieRepository(cpt *component.Component, ctx context.Context) (domain.TrieRepository, error) {
	var tR trieRepository
	tR.Redis = cpt.Dao.Redis
	tR.GormDB = cpt.Dao.GormDB
	_, err := tR.Redis.Ping(ctx).Result()
	if err != nil {
		return nil, err
	}

	version, err := tR.Redis.Get(ctx, "trie_version").Result()
	if err != nil && err != redis.Nil {
		return nil, err
	}
	if version == "" {
		version = "1.0"
	}
	tR.version = version

	trie, err := tR.GetTrie(ctx)
	if err != nil {
		return nil, err
	}
	tR.Tire = trie
	return &tR, nil
}

func (r *trieRepository) GetTrie(ctx context.Context) (*Trie, error) {
	// Get trie from Redis
	trieJSON, err := r.Redis.Get(ctx, "trie").Result()
	if err != nil {
		return nil, err
	}

	// Unmarshal trie from JSON to Go struct
	trie, err := Deserialize([]byte(trieJSON))
	if err != nil {
		return nil, err
	}
	r.Tire = trie
	return trie, nil
}

func (r *trieRepository) SaveTrie(trie *Trie) error {
	// Marshal trie struct to JSON
	trieJSON, err := Serialize(trie)
	if err != nil {
		return err
	}
	// Save trie to Redis
	err = r.Redis.Set(r.Ctx, "trie", trieJSON, 0).Err()
	if err != nil {
		return err
	}

	return nil
}

func (r *trieRepository) Insert(word string, id int64) error {
	// Insert word into trie
	r.Tire.Insert(word, id)

	// Save updated trie to Redis
	return r.SaveTrie(r.Tire)
}

func (r *trieRepository) TrieSearch(ctx context.Context, qf domain.QuestionForm) (int64, bool) {
	// Search for word in trie
	return r.Tire.Search(qf.Keyword)
}

func (r *trieRepository) GetVersion(ctx context.Context) string {
	return r.version
}
func (r *trieRepository) FindByID(ctx context.Context, id int64) (knowledge *domain.Knowledge, err error) {
	// Find knowledge by id
	err = r.GormDB.Select("id = ?", id).Find(&knowledge).Error
	return
}

func (r *trieRepository) Delete(word string) error {
	// Delete word from trie
	r.Tire.Remove(word)

	// Save updated trie to Redis
	return r.SaveTrie(r.Tire)
}

func (r *trieRepository) Update(word string, newWord string) error {
	// Delete word from trie
	r.Tire.Update(word, newWord)

	// Save updated trie to Redis
	return r.SaveTrie(r.Tire)
}
