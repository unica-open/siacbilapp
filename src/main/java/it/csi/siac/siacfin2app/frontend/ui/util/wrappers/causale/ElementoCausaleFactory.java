/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.util.wrappers.causale;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.BaseFactory;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siacfin2ser.model.Causale;
import it.csi.siac.siacfin2ser.model.StatoOperativoCausale;
import it.csi.siac.siacfin2ser.model.TipoCausale;
import it.csi.siac.siacfinser.model.MovimentoGestione;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

/**
 * Factory per il wrapping delle Causali.
 * 
 * @author Osorio Alessandra
 * @version 1.0.0 - 17/04/2014
 */
public class ElementoCausaleFactory extends BaseFactory {
	
	/**
	 * Crea un'istanza del wrapper a partire dalla Causale.
	 * @param <C> la tipizzazione della causale
	 * @param <CAP> la tipizzazione del capitolo
	 * @param <MG> la tipizzazione del movimento di gestione
	 * @param <SMG> la tipizzazione del submovimento di gestione
	 * 
	 * @param causale                               la causale da wrappare
	 * @param wrapper                               il wrapper da popolare
	 * @param capitolo                              il capitolo
	 * @param movimentoGestione                     il movimento di gestione
	 * @param subMovimentoGestione                  il subMovimento di gestione
	 * @param tipoAtto                              il tipo di atto
	 * @param strutturaAmministrativoContabile      la struttura amministrativo contabile
	 * @param gestioneUEB                           se l'ente gestisce le UEB
	 */
	protected static <C extends Causale, CAP extends Capitolo<?, ?>, MG extends MovimentoGestione, SMG extends MG> void populateAttributes(C causale, 
			ElementoCausale wrapper, CAP capitolo, MG movimentoGestione, SMG subMovimentoGestione, TipoAtto tipoAtto,
			StrutturaAmministrativoContabile strutturaAmministrativoContabile, Boolean gestioneUEB) {
		
		Integer uid = null;
		String codice = "";
		String descrizione = "";
		String tipo = "";
		String dataDecorrenza = "";
		String statoOperativoCausaleCode = "";
		String statoOperativoCausaleDesc = "";
		String struttura = "";
		String soggettoWrapper = "";
		StringBuilder cap = new StringBuilder();
		StringBuilder movgest = new StringBuilder();
		StringBuilder provvedimento = new StringBuilder();
		
		uid = causale.getUid();
		codice = causale.getCodice();
		descrizione = causale.getDescrizione();
		if(causale.getTipoCausale() != null) {
			TipoCausale tc = causale.getTipoCausale();
			tipo = tc.getCodice() + " - " + tc.getDescrizione();
		}
		dataDecorrenza = causale.getDataFineValidita() != null ? FormatUtils.formatDate(causale.getDataFineValidita()) : FormatUtils.formatDate(causale.getDataCreazione());
		if(causale.getStatoOperativoCausale() != null) {
			StatoOperativoCausale soc = causale.getStatoOperativoCausale();
			statoOperativoCausaleCode = soc.getCodice();
			statoOperativoCausaleDesc = soc.getDescrizione();
		}
		if(causale.getStrutturaAmministrativoContabile() != null) {
			StrutturaAmministrativoContabile sac = causale.getStrutturaAmministrativoContabile();
			struttura = sac.getCodice() + " - " + sac.getDescrizione();
		}
		if(causale.getSoggetto() != null) {
			Soggetto s = causale.getSoggetto();
			soggettoWrapper = s.getCodiceSoggetto() + " - " + s.getDenominazione();
		}
		if(capitolo != null) {
			cap.append(capitolo.getAnnoCapitolo())
				.append("/")
				.append(capitolo.getNumeroCapitolo())
				.append("/")
				.append(capitolo.getNumeroArticolo());
			if(Boolean.TRUE.equals(gestioneUEB)) {
				cap.append("/")
					.append(capitolo.getNumeroUEB());
			}
		}
		if(movimentoGestione != null) {
			movgest.append(movimentoGestione.getAnnoMovimento())
				.append("/")
				.append(movimentoGestione.getNumero());
			if(subMovimentoGestione != null) {
				movgest.append("-")
					.append(subMovimentoGestione.getNumero());
			}
		}
		if(causale.getAttoAmministrativo() != null) {
			AttoAmministrativo aa = causale.getAttoAmministrativo();
			provvedimento.append(aa.getAnno())
				.append("/")
				.append(causale.getAttoAmministrativo().getNumero())
				.append("/")
				.append(tipoAtto.getDescrizione());
			if(causale.getAttoAmministrativo().getStrutturaAmmContabile() != null) {
				provvedimento.append("/")
					.append(causale.getAttoAmministrativo().getStrutturaAmmContabile().getCodice());
			}
		}
		
		wrapper.setUid(uid);
		wrapper.setCausaleCode(codice);
		wrapper.setCausaleDesc(descrizione);
		wrapper.setTipoCausale(tipo);
		wrapper.setDataDecorrenza(dataDecorrenza);
		wrapper.setStatoOperativoCausaleCode(statoOperativoCausaleCode);
		wrapper.setStatoOperativoCausaleDesc(statoOperativoCausaleDesc);
		wrapper.setStrutturaAmministrativa(struttura);
		wrapper.setSoggetto(soggettoWrapper);
		wrapper.setCapitolo(cap.toString());
		wrapper.setMovimentoGestione(movgest.toString());
		wrapper.setProvvedimento(provvedimento.toString());
	}
	
}
