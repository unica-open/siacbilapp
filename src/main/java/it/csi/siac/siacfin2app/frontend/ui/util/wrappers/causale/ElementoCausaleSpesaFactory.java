/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.util.wrappers.causale;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siacfin2ser.model.CausaleSpesa;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;

/**
 * Factory per il wrapping delle Causali.
 * 
 * @author Osorio Alessandra
 * @version 1.0.0 - 17/04/2014
 */
public final class ElementoCausaleSpesaFactory extends ElementoCausaleFactory {
	
	/** Non permettere l'instanziazione della classe */
	private ElementoCausaleSpesaFactory() {
	}
	
	/**
	 * Crea un'istanza del wrapper a partire dalla Causale.
	 * @param <D> il tipo della causale di spesa
	 * @param causale                               la causale da wrappare
	 * @param listaTipoAtto                         la lista dei tipi atto per recuperare la descrizione
	 * @param listaStrutturaAmministrativoContabile la lista delle struture amministrative per recuperare la descrizione
	 * @param gestioneUEB                           se l'ente gestisce le UEB
	 * 
	 * @return il wrapper creato
	 */
	public static <D extends CausaleSpesa> ElementoCausale getInstance(D causale, List<TipoAtto> listaTipoAtto, 
			List<StrutturaAmministrativoContabile> listaStrutturaAmministrativoContabile, Boolean gestioneUEB) {
		
		ElementoCausale result = new ElementoCausale();
		
		CapitoloUscitaGestione capitolo = causale.getCapitoloUscitaGestione();
		Impegno impegno = causale.getImpegno();
		SubImpegno subImpegno = causale.getSubImpegno();
		AttoAmministrativo attoAmministrativo = causale.getAttoAmministrativo();
		SedeSecondariaSoggetto sedeSecondariaSoggetto = causale.getSedeSecondariaSoggetto();
		ModalitaPagamentoSoggetto modPagSoggetto = causale.getModalitaPagamentoSoggetto();
		
		TipoAtto tipoAtto = null;
		StrutturaAmministrativoContabile strutturaAmministrativoContabile = null;
		
		if(attoAmministrativo != null) {
			tipoAtto = ComparatorUtils.searchByUid(listaTipoAtto, attoAmministrativo.getTipoAtto());
			strutturaAmministrativoContabile = ComparatorUtils.searchByUidWithChildren(listaStrutturaAmministrativoContabile, 
					attoAmministrativo.getStrutturaAmmContabile());
		}
		
		populateAttributes(causale, result, capitolo, impegno, subImpegno, tipoAtto, strutturaAmministrativoContabile, gestioneUEB);
		
		if(sedeSecondariaSoggetto != null) {
			String sede = sedeSecondariaSoggetto.getCodiceSedeSecondaria() + " - " + sedeSecondariaSoggetto.getDenominazione();
			result.setSediSecondarie(sede);
		}
		
		if(modPagSoggetto != null) {
			popolaModalitaPagamentoSoggetto(result, modPagSoggetto);
		}
		
		return result;
	}
	
	/**
	 * Popolamento della modalit&agrave; di pagamento del soggetto. Template:
	 * <br/>
	 * <code>(codice - descrizione) iban: &lt;...&gt; - bic &lt;...&gt; - conto &lt;...&gt; - intestato a &lt;...&gt; 
	 * - quietanzante: &lt;...&gt; - CF: &lt;...&gt; - nato il &lt;...&gt; a &lt;comune e nazione&gt; </code>
	 * 
	 * @param wrapper                   il wrapper per la causale
	 * @param modalitaPagamentoSoggetto la modalita da impostare
	 */
	private static void popolaModalitaPagamentoSoggetto(ElementoCausale wrapper, ModalitaPagamentoSoggetto modalitaPagamentoSoggetto) {
		// SIAC-5156: modifica gestione descrizione modalita' pagamento soggetto
		String str = modalitaPagamentoSoggetto != null && modalitaPagamentoSoggetto.getDescrizioneInfo() != null
				? modalitaPagamentoSoggetto.getDescrizioneInfo().getDescrizioneArricchita()
				: null;
		wrapper.setModalitaPagamentoSoggetto(str);
	}

	/**
	 * Ottiene istanze del wrapper a partire dalle causale
	 * 
	 * @param causali                               le causali da wrappare
	 * @param listaTipoAtto                         la lista dei tipi atto per recuperare la descrizione
	 * @param listaStrutturaAmministrativoContabile la lista delle struture amministrative per recuperare la descrizione
	 * @param gestioneUEB                           se l'ente gestisce le UEB
	 * 
	 * @return i wrapper creati
	 */
	public static List<ElementoCausale> getInstances (List<CausaleSpesa> causali, List<TipoAtto> listaTipoAtto,
			List<StrutturaAmministrativoContabile> listaStrutturaAmministrativoContabile, Boolean gestioneUEB) {
		List<ElementoCausale> result = new ArrayList<ElementoCausale>();

		for(CausaleSpesa causale : causali) {
			result.add(getInstance(causale, listaTipoAtto, listaStrutturaAmministrativoContabile, gestioneUEB));
		}
		
		return result;
	}
	
}
