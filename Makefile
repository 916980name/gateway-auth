COMMON_SELF_DIR := $(dir $(lastword $(MAKEFILE_LIST)))
ROOT_DIR := $(abspath $(shell cd $(COMMON_SELF_DIR) && pwd -P))
root: 
	@echo $(ROOT_DIR)

.PHONY: dep
dep:
	$(ROOT_DIR)/gradlew dependencies > $(ROOT_DIR)/dependency.txt