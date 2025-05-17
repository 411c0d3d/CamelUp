package com.oasisstudios.camelupserver.domain.model.domainclasses;
  
    public class BoardSpaceDOM {
    
        private int spaceId;
        private PlayerCardDOM playerCardDOM; // Optional to represent possible absence
        private CamelPackDOM camelPackDOM; // Optional to represent possible absence
    
        // Constructor
        public BoardSpaceDOM(int spaceId) {

            this.spaceId = spaceId;
            this.camelPackDOM = new CamelPackDOM();
            this.playerCardDOM = null; // No PlayerCardDOM initially
        }
    
        // Getter for spaceId
        public int getSpaceId() {
            return this.spaceId;
        }
    
        // Setter for spaceId
        public void setSpaceId(int spaceId) {
            this.spaceId = spaceId;
        }
    
        // Getter for CamelPackDOM
        public CamelPackDOM getCamelPack() {
            return this.camelPackDOM;
        }
    
        // Setter for CamelPackDOM
        public void setCamelPack(CamelPackDOM camelPackDOM) {
            this.camelPackDOM = camelPackDOM;
        }

        public PlayerCardDOM getPlayerCard() {
            if (this.spaceId == 0 || this.spaceId == 1) {
                throw new IllegalStateException("Cannot get PlayerCardDOM for space ID 0 or 1.");
            }
            return this.playerCardDOM;
        }
    
        // Getter for PlayerCardDOM
        public void removePlayerCard() {
            this.playerCardDOM = null;
        }

        public boolean hasPlayerCard() {
            return this.playerCardDOM != null;
        }

    
        // Setter for PlayerCardDOM
        public void setPlayerCard(PlayerCardDOM playerCardDOM) {
            if (this.spaceId == 0 || this.spaceId == 1) {
                throw new IllegalStateException("Cannot set PlayerCardDOM for space ID 0 or 1.");
            }
            this.playerCardDOM = playerCardDOM;
        }


    }
    
    


