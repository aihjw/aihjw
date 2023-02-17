package question

import (
	"bytes"
	"encoding/gob"
	"strings"
)

// TrieNode 字典树的节点
type TrieNode struct {
	// 是否是单词的结尾
	isEnd bool
	// 存储的信息，这里存储id
	id int64
	// 字典树的下一个节点
	next map[rune]*TrieNode
}

// Trie 字典树
type Trie struct {
	root *TrieNode
}

// NewTrie 创建字典树
func NewTrie() *Trie {
	return &Trie{
		root: &TrieNode{
			next: make(map[rune]*TrieNode),
		}}
}

// Insert 插入一个单词
func (t *Trie) Insert(word string, id int64) bool {
	if _, ok := t.Search(word); ok {
		return false
	}

	node := t.root
	for _, char := range word {
		if node.next[char] == nil {
			node.next[char] = &TrieNode{next: make(map[rune]*TrieNode)}
		}
		node = node.next[char]
	}
	node.isEnd = true
	node.id = id
	return true
}

// Search 查询一个单词是否在字典树中，返回id
func (t *Trie) Search(word string) (int64, bool) {
	node := t.root
	for _, char := range word {
		if node.next[char] == nil {
			return 0, false
		}
		node = node.next[char]
	}
	if node.isEnd {
		return node.id, true
	}
	return 0, false
}

// SearchNode 查询一个单词是否在字典树中，返回末尾节点
func (t *Trie) SearchNode(word string) (bool, *TrieNode) {
	node := t.root
	for _, char := range word {
		if node.next[char] == nil {
			return false, nil
		}
		node = node.next[char]
	}
	if node.isEnd {
		return true, node
	}
	return false, nil
}

// Update 更新单词
func (t *Trie) Update(word string, newWord string) bool {
	if newWord == word {
		return true
	}
	isExistOld, node := t.SearchNode(word)
	isExistNew, _ := t.SearchNode(newWord)
	if !isExistOld || isExistNew {
		return false
	}

	id := node.id
	if strings.HasPrefix(newWord, word) {
		newWord = strings.TrimPrefix(newWord, word)
		node.isEnd = false
		node.id = 0
		for i, char := range newWord {
			if node.next[char] == nil {
				node.next[char] = &TrieNode{next: make(map[rune]*TrieNode)}
			}
			node = node.next[char]
			if i == len(newWord)-1 {
				node.isEnd = true
				node.id = id
			}
		}
		return true
	}
	t.Remove(word)
	t.Insert(newWord, node.id)
	return true
}

// Remove 删除单词
func (t *Trie) Remove(word string) {
	node := t.root
	stack := []*TrieNode{node}
	for i, char := range word {
		if node.next[char] == nil {
			return
		}
		stack = append(stack, node.next[char])
		node = node.next[char]
		if i == len(word)-1 && node.isEnd {

			node.isEnd = false
			node.id = 0
			// 删除无用的节点
			for i := len(stack) - 2; i >= 0; i-- {
				node = stack[i]
				if node.isEnd || len(node.next) > 1 {
					break
				}
				delete(node.next, rune(word[i]))
			}
		}
	}
}

// Serialize 序列化trie
func Serialize(t *Trie) ([]byte, error) {
	var b bytes.Buffer
	enc := gob.NewEncoder(&b)
	if err := enc.Encode(t); err != nil {
		return nil, err
	}
	return b.Bytes(), nil
}

// Deserialize 反序列化trie
func Deserialize(data []byte) (t *Trie, err error) {
	b := bytes.NewBuffer(data)
	dec := gob.NewDecoder(b)
	if err := dec.Decode(&t); err != nil {
		return nil, err
	}
	return t, nil
}
